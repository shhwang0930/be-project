package com.shhwang0930.beproject.repository;

import com.shhwang0930.beproject.entity.CpuUsageHour;
import com.shhwang0930.beproject.entity.CpuUsageMinute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CpuUsageHourRepository extends JpaRepository<CpuUsageHour,Long> {
    List<CpuUsageHour> findAllByTimestampBetween(LocalDateTime s, LocalDateTime e);
    void deleteByTimestampBefore(LocalDateTime t);
}
