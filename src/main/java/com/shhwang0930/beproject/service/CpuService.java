package com.shhwang0930.beproject.service;

import com.shhwang0930.beproject.entity.CpuUsageDay;
import com.shhwang0930.beproject.entity.CpuUsageHour;
import com.shhwang0930.beproject.entity.CpuUsageMinute;
import com.shhwang0930.beproject.repository.CpuUsageDayRepository;
import com.shhwang0930.beproject.repository.CpuUsageHourRepository;
import com.shhwang0930.beproject.repository.CpuUsageMinuteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CpuService {
    private final CpuUsageMinuteRepository cpuUsageMinuteRepository;
    private final CpuUsageHourRepository cpuUsageHourRepository;
    private final CpuUsageDayRepository cpuUsageDayRepository;
    private final MetricsEndpoint metricsEndpoint;

    // 분 단위로 cpu 사용률 측정 및 저장
    @Scheduled(fixedRate = 60000)
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
        // s와 e를 비교해서 예외처리 하도록
        return cpuUsageMinuteRepository.findAllByTimestampBetween(s,e);
    }



    // 일 단위의 cpu 사용률 저장
}
