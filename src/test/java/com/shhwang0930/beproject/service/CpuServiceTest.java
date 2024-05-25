package com.shhwang0930.beproject.service;

import com.shhwang0930.beproject.entity.CpuUsageDay;
import com.shhwang0930.beproject.entity.CpuUsageHour;
import com.shhwang0930.beproject.entity.CpuUsageMinute;
import com.shhwang0930.beproject.repository.CpuUsageDayRepository;
import com.shhwang0930.beproject.repository.CpuUsageHourRepository;
import com.shhwang0930.beproject.repository.CpuUsageMinuteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CpuServiceTest {

    @InjectMocks
    private CpuService cpuService;

    @Mock
    private CpuUsageMinuteRepository cpuUsageMinuteRepository;

    @Mock
    private CpuUsageHourRepository cpuUsageHourRepository;

    @Mock
    private CpuUsageDayRepository cpuUsageDayRepository;


    @Test
    @DisplayName("CPU 사용률 저장 - 성공")
    public void testSaveCpuUsage() {
        // given
        double cpuUsage = 60.5;
        // when
        cpuService.saveCpuUsage(cpuUsage);
        // then
        verify(cpuUsageMinuteRepository, times(1)).save(any(CpuUsageMinute.class));
    }

    @Test
    @DisplayName("분당 CPU 사용률 조회 - 성공")
    void testGetCpuUsageMinute() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        CpuUsageMinute usage1 = new CpuUsageMinute();
        usage1.setTimestamp(startTime.plusMinutes(10));
        usage1.setUsage(30.0);

        CpuUsageMinute usage2 = new CpuUsageMinute();
        usage2.setTimestamp(startTime.plusMinutes(20));
        usage2.setUsage(40.0);

        List<CpuUsageMinute> minuteUsages = Arrays.asList(usage1, usage2);

        when(cpuUsageMinuteRepository.findAllByTimestampBetween(startTime, endTime))
                .thenReturn(minuteUsages);

        // when
        List<CpuUsageMinute> result = cpuService.getCpuUsageMinute(startTime, endTime);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(cpuUsageMinuteRepository, times(1)).findAllByTimestampBetween(startTime, endTime);
    }

    @Test
    @DisplayName("분당 CPU 사용률 조회 - 성공(데이터 없는 경우)")
    void testGetCpuUsageMinute_empty() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        when(cpuUsageMinuteRepository.findAllByTimestampBetween(startTime, endTime))
                .thenReturn(Collections.emptyList());

        // when
        List<CpuUsageMinute> result = cpuService.getCpuUsageMinute(startTime, endTime);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cpuUsageMinuteRepository, times(1)).findAllByTimestampBetween(startTime, endTime);
    }

    @Test
    @DisplayName("분당 CPU 사용률 조회 - 실패(시간을 잘못 입력한 경우)")
    void testGetCpuUsageMinute_invalid_time_range() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().minusHours(1);

        // when & then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            cpuService.getCpuUsageMinute(startTime, endTime);
        });
        assertEquals("getCpuUsageMinute 메서드 실행 중 예외 발생", thrown.getMessage());
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertEquals("시작 시간이 종료 시간보다 늦습니다.", thrown.getCause().getMessage());
        verify(cpuUsageMinuteRepository, never()).findAllByTimestampBetween(any(), any());
    }

    @Test
    @DisplayName("분당 CPU 사용률 조회 - 실패(런타임에러)")
    void testGetCpuUsageMinute_runtime_exception() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        when(cpuUsageMinuteRepository.findAllByTimestampBetween(startTime, endTime))
                .thenThrow(new RuntimeException("Database error"));

        // when & then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            cpuService.getCpuUsageMinute(startTime, endTime);
        });
        assertEquals("getCpuUsageMinute 메서드 실행 중 예외 발생", thrown.getMessage());
        verify(cpuUsageMinuteRepository, times(1)).findAllByTimestampBetween(startTime, endTime);
    }

    // 시간당
    @Test
    @DisplayName("시간당 CPU 사용률 조회 - 성공")
    void testGetCpuUsageHour() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();

        CpuUsageHour usage1 = new CpuUsageHour();
        usage1.setTimestamp(startTime.plusHours(1));
        usage1.setMin(10.0);
        usage1.setMax(50.0);
        usage1.setAvr(30.0);

        CpuUsageHour usage2 = new CpuUsageHour();
        usage2.setTimestamp(startTime.plusHours(2));
        usage2.setMin(20.0);
        usage2.setMax(60.0);
        usage2.setAvr(40.0);

        List<CpuUsageHour> hourUsages = Arrays.asList(usage1, usage2);

        when(cpuUsageHourRepository.findAllByTimestampBetween(startTime, endTime))
                .thenReturn(hourUsages);

        // when
        List<CpuUsageHour> result = cpuService.getCpuUsageHour(startTime, endTime);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(cpuUsageHourRepository, times(1)).findAllByTimestampBetween(startTime, endTime);
    }

    @Test
    @DisplayName("시간당 CPU 사용률 조회 - 성공(데이터가 없는 경우)")
    void testGetCpuUsageHour_empty() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();

        when(cpuUsageHourRepository.findAllByTimestampBetween(startTime, endTime))
                .thenReturn(Collections.emptyList());

        // when
        List<CpuUsageHour> result = cpuService.getCpuUsageHour(startTime, endTime);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cpuUsageHourRepository, times(1)).findAllByTimestampBetween(startTime, endTime);
    }

    @Test
    @DisplayName("시간당 CPU 사용률 조회 - 실패(시간을 잘못 입력한 경우)")
    void testGetCpuUsageHour_invalid_time_range() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().minusDays(1);

        // when & then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            cpuService.getCpuUsageHour(startTime, endTime);
        });
        assertEquals("getCpuUsageHour 메서드 실행 중 예외 발생", thrown.getMessage());
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertEquals("시작 시간이 종료 시간보다 늦습니다.", thrown.getCause().getMessage());
        verify(cpuUsageHourRepository, never()).findAllByTimestampBetween(any(), any());
    }

    @Test
    @DisplayName("시간당 CPU 사용률 조회 - 실패(런타임에러)")
    void testGetCpuUsageHour_runtime_exception() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();

        when(cpuUsageHourRepository.findAllByTimestampBetween(startTime, endTime))
                .thenThrow(new RuntimeException("Database error"));

        // when & then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            cpuService.getCpuUsageHour(startTime, endTime);
        });
        assertEquals("getCpuUsageHour 메서드 실행 중 예외 발생", thrown.getMessage());
        verify(cpuUsageHourRepository, times(1)).findAllByTimestampBetween(startTime, endTime);
    }

    @Test
    @DisplayName("일당 CPU 사용률 조회 - 성공")
    void testGetCpuUsageDay() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();

        CpuUsageDay usage1 = new CpuUsageDay();
        usage1.setTimestamp(startTime.plusDays(1));
        usage1.setMin(10.0);
        usage1.setMax(50.0);
        usage1.setAvr(30.0);

        CpuUsageDay usage2 = new CpuUsageDay();
        usage2.setTimestamp(startTime.plusDays(2));
        usage2.setMin(20.0);
        usage2.setMax(60.0);
        usage2.setAvr(40.0);

        List<CpuUsageDay> dayUsages = Arrays.asList(usage1, usage2);

        when(cpuUsageDayRepository.findAllByTimestampBetween(startTime, endTime))
                .thenReturn(dayUsages);

        // when
        List<CpuUsageDay> result = cpuService.getCpuUsageDay(startTime, endTime);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(cpuUsageDayRepository, times(1)).findAllByTimestampBetween(startTime, endTime);
    }

    @Test
    @DisplayName("일당 CPU 사용률 조회 - 성공(데이터가 없는 경우)")
    void testGetCpuUsageDay_empty() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();

        when(cpuUsageDayRepository.findAllByTimestampBetween(startTime, endTime))
                .thenReturn(Collections.emptyList());

        // when
        List<CpuUsageDay> result = cpuService.getCpuUsageDay(startTime, endTime);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(cpuUsageDayRepository, times(1)).findAllByTimestampBetween(startTime, endTime);
    }

    @Test
    @DisplayName("일당 CPU 사용률 조회 - 실패(시간을 잘못 입력한 경우)")
    void testGetCpuUsageDay_invalid_time_range() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().minusDays(1);

        // when & then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            cpuService.getCpuUsageDay(startTime, endTime);
        });
        assertEquals("getCpuUsageDay 메서드 실행 중 예외 발생", thrown.getMessage());
        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
        assertEquals("시작 시간이 종료 시간보다 늦습니다.", thrown.getCause().getMessage());
        verify(cpuUsageDayRepository, never()).findAllByTimestampBetween(any(), any());
    }

    @Test
    @DisplayName("일당 CPU 사용률 조회 - 실패(런타임에러)")
    void testGetCpuUsageDay_runtime_exception() {
        // given
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();

        when(cpuUsageDayRepository.findAllByTimestampBetween(startTime, endTime))
                .thenThrow(new RuntimeException("Database error"));

        // when & then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            cpuService.getCpuUsageDay(startTime, endTime);
        });
        assertEquals("getCpuUsageDay 메서드 실행 중 예외 발생", thrown.getMessage());
        verify(cpuUsageDayRepository, times(1)).findAllByTimestampBetween(startTime, endTime);
    }

    @Test
    @DisplayName("시간당 CPU 사용률 저장 - 성공")
    void testSaveHourUsage() {
        // given
        LocalDateTime startOfHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).minusHours(1);
        LocalDateTime endOfHour = startOfHour.plusHours(1);

        CpuUsageMinute usage1 = new CpuUsageMinute();
        usage1.setTimestamp(startOfHour.plusMinutes(10));
        usage1.setUsage(30.0);

        CpuUsageMinute usage2 = new CpuUsageMinute();
        usage2.setTimestamp(startOfHour.plusMinutes(20));
        usage2.setUsage(40.0);

        CpuUsageMinute usage3 = new CpuUsageMinute();
        usage3.setTimestamp(startOfHour.plusMinutes(30));
        usage3.setUsage(50.0);

        List<CpuUsageMinute> minuteUsages = Arrays.asList(usage1, usage2, usage3);

        when(cpuUsageMinuteRepository.findAllByTimestampBetween(startOfHour, endOfHour))
                .thenReturn(minuteUsages);

        // when
        cpuService.saveHourUsage();

        // then
        verify(cpuUsageHourRepository, times(1)).save(any(CpuUsageHour.class));
    }

    @Test
    @DisplayName("시간당 CPU 사용률 저장  - 성공(데이터 없을 경우)")
    void testSaveHourUsage_empty() {
        // given
        LocalDateTime startOfHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0).minusHours(1);
        LocalDateTime endOfHour = startOfHour.plusHours(1);

        when(cpuUsageMinuteRepository.findAllByTimestampBetween(startOfHour, endOfHour))
                .thenReturn(Collections.emptyList());

        // when
        cpuService.saveHourUsage();

        // then
        verify(cpuUsageHourRepository, never()).save(any(CpuUsageHour.class));
    }

    @Test
    @DisplayName("일당 CPU 사용률 저장 - 성공")
    void testSaveDayUsage() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(1);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        CpuUsageHour usage1 = new CpuUsageHour();
        usage1.setTimestamp(startOfDay.plusHours(1));
        usage1.setMin(10.0);
        usage1.setMax(50.0);
        usage1.setAvr(30.0);

        CpuUsageHour usage2 = new CpuUsageHour();
        usage2.setTimestamp(startOfDay.plusHours(2));
        usage2.setMin(20.0);
        usage2.setMax(60.0);
        usage2.setAvr(40.0);

        List<CpuUsageHour> hourlyUsages = Arrays.asList(usage1, usage2);

        when(cpuUsageHourRepository.findAllByTimestampBetween(startOfDay, endOfDay))
                .thenReturn(hourlyUsages);

        // when
        cpuService.saveDayUsage();

        // then
        verify(cpuUsageDayRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("일당 CPU 사용률 저장  - 성공(데이터 없을 경우)")
    void testSaveDayUsage_empty() {
        // given
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(1);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        when(cpuUsageHourRepository.findAllByTimestampBetween(startOfDay, endOfDay))
                .thenReturn(Collections.emptyList());

        // when
        cpuService.saveDayUsage();

        // then
        verify(cpuUsageDayRepository, never()).save(any());
    }

    @Test
    @DisplayName("1주뒤 분단위 사용률 삭제")
    public void testDeleteMinute() {
        // Given
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

        // When
        cpuService.deleteMinute();

        // Then
        verify(cpuUsageMinuteRepository, times(1)).deleteByTimestampBefore(oneWeekAgo);
    }

    @Test
    @DisplayName("3달뒤 시단위 사용률 삭제")
    public void testDeleteHour() {
        // Given
        LocalDateTime threeMonthAgo = LocalDateTime.now().minusMonths(3);

        // When
        cpuService.deleteHour();

        // Then
        verify(cpuUsageHourRepository, times(1)).deleteByTimestampBefore(threeMonthAgo);
    }

    @Test
    @DisplayName("1년뒤 일단위 사용률 삭제")
    public void testDeleteDay() {
        // Given
        LocalDateTime oneYearAgo = LocalDateTime.now().minusYears(1);

        // When
        cpuService.deleteDay();

        // Then
        verify(cpuUsageDayRepository, times(1)).deleteByTimestampBefore(oneYearAgo);
    }
}