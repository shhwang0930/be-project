package com.shhwang0930.beproject.service;

import com.shhwang0930.beproject.entity.CpuUsageDay;
import com.shhwang0930.beproject.entity.CpuUsageHour;
import com.shhwang0930.beproject.entity.CpuUsageMinute;
import com.shhwang0930.beproject.repository.CpuUsageDayRepository;
import com.shhwang0930.beproject.repository.CpuUsageHourRepository;
import com.shhwang0930.beproject.repository.CpuUsageMinuteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CpuService {
    private final CpuUsageMinuteRepository cpuUsageMinuteRepository;
    private final CpuUsageHourRepository cpuUsageHourRepository;
    private final CpuUsageDayRepository cpuUsageDayRepository;
    private final MetricsEndpoint metricsEndpoint;

    // 분 단위로 cpu 사용률 측정 및 저장
    @Scheduled(fixedRate = 5000)
    @Transactional
    public void checkCpuUsage() {
        MetricsEndpoint.MetricResponse metric = metricsEndpoint.metric("system.cpu.usage", null);
        if (metric != null && !metric.getMeasurements().isEmpty()) {
            double cpuUsage = metric.getMeasurements().get(0).getValue() * 100;
            saveCpuUsage(cpuUsage);
        }
    }

    public void saveCpuUsage(double cpuUsage){
        CpuUsageMinute cpuUsageMinute = new CpuUsageMinute();
        cpuUsageMinute.setUsage(cpuUsage);
        cpuUsageMinute.setTimestamp(LocalDateTime.now());
        cpuUsageMinuteRepository.save(cpuUsageMinute);
    }


    // 분 단위 cpu 조회
    public List<CpuUsageMinute> getCpuUsageMinute(LocalDateTime s, LocalDateTime e){
        try {
            if (s.isAfter(e)) {
                throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦습니다.");
            }
            return cpuUsageMinuteRepository.findAllByTimestampBetween(s, e);
        } catch (Exception ex) {
            log.error("getCpuUsageMinute 메서드에서 예외 발생", ex);
            throw new RuntimeException("getCpuUsageMinute 메서드 실행 중 예외 발생", ex);
        }
    }

    // 시 단위의 cpu 사용률 조회
    public List<CpuUsageHour> getCpuUsageHour(LocalDateTime s, LocalDateTime e){
        try {
            if (s.isAfter(e)) {
                throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦습니다.");
            }
            return cpuUsageHourRepository.findAllByTimestampBetween(s,e);
        } catch (Exception ex) {
            log.error("getCpuUsageHour 메서드에서 예외 발생", ex);
            throw new RuntimeException("getCpuUsageHour 메서드 실행 중 예외 발생", ex);
        }
    }

    public List<CpuUsageDay> getCpuUsageDay(LocalDateTime s, LocalDateTime e){
        try {
            if (s.isAfter(e)) {
                throw new IllegalArgumentException("시작 시간이 종료 시간보다 늦습니다.");
            }
            return cpuUsageDayRepository.findAllByTimestampBetween(s,e);
        } catch (Exception ex) {
            log.error("getCpuUsageDay 메서드에서 예외 발생", ex);
            throw new RuntimeException("getCpuUsageDay 메서드 실행 중 예외 발생", ex);
        }
    }

    // 시 단위 집계 작업 (1시간마다 실행)
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void saveHourUsage() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfHour = now.withMinute(0).withSecond(0).withNano(0).minusHours(1);
            LocalDateTime endOfHour = startOfHour.plusHours(1);

            List<CpuUsageMinute> minuteUsages = cpuUsageMinuteRepository.findAllByTimestampBetween(startOfHour,endOfHour);
            if (!minuteUsages.isEmpty()) {
                double minUsage = minuteUsages.stream().mapToDouble(CpuUsageMinute::getUsage).min().orElse(0);
                double maxUsage = minuteUsages.stream().mapToDouble(CpuUsageMinute::getUsage).max().orElse(0);
                double avrUsage = minuteUsages.stream().mapToDouble(CpuUsageMinute::getUsage).average().orElse(0);

                CpuUsageHour hourlyUsage = new CpuUsageHour();
                hourlyUsage.setTimestamp(startOfHour);
                hourlyUsage.setMin(minUsage);
                hourlyUsage.setMax(maxUsage);
                hourlyUsage.setAvr(avrUsage);
                cpuUsageHourRepository.save(hourlyUsage);
            }
        } catch (Exception ex) {
            log.error("saveHourUsage 메서드에서 예외 발생", ex);
        }
    }

    // 일 단위 집계 작업 (매일 자정 실행)
    @Scheduled(cron = "0 0 0 * * *")
    public void saveDayUsage() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(1);
            LocalDateTime endOfDay = startOfDay.plusDays(1);

            List<CpuUsageHour> hourlyUsages = cpuUsageHourRepository.findAllByTimestampBetween(startOfDay, endOfDay);
            if (!hourlyUsages.isEmpty()) {
                double minUsage = hourlyUsages.stream().mapToDouble(CpuUsageHour::getMin).min().orElse(0);
                double maxUsage = hourlyUsages.stream().mapToDouble(CpuUsageHour::getMax).max().orElse(0);
                double avrUsage = hourlyUsages.stream().mapToDouble(CpuUsageHour::getAvr).average().orElse(0);

                CpuUsageDay dailyUsage = new CpuUsageDay();
                dailyUsage.setTimestamp(startOfDay);
                dailyUsage.setMin(minUsage);
                dailyUsage.setMax(maxUsage);
                dailyUsage.setAvr(avrUsage);
                cpuUsageDayRepository.save(dailyUsage);
            }
        } catch (Exception ex) {
            log.error("saveDayUsage 메서드에서 예외 발생", ex);
        }
    }

    // 매일 자정에 실행 (분 단위 데이터 1주 후 삭제)
    @Scheduled(cron = "0 0 0 * * *")
    public void deleteMinute() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        cpuUsageMinuteRepository.deleteByTimestampBefore(oneWeekAgo);
    }

    // 매월 1일 자정에 실행 (시 단위 데이터 3개월 후 삭제)
    @Scheduled(cron = "0 0 0 1 * *")
    public void deleteHour() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        cpuUsageHourRepository.deleteByTimestampBefore(threeMonthsAgo);
    }

    // 매년 1월 1일 자정에 실행 (일 단위 데이터 1년 후 삭제)
    @Scheduled(cron = "0 0 0 1 1 *")
    public void deleteDay() {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);
        cpuUsageDayRepository.deleteByTimestampBefore(oneYearAgo);
    }
}
