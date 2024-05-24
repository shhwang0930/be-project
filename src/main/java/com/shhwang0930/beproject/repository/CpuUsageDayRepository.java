package com.shhwang0930.beproject.repository;

import com.shhwang0930.beproject.entity.CpuUsageDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CpuUsageDayRepository extends JpaRepository<CpuUsageDay,Long> {
    List<CpuUsageDay> findAllByTimestampBetween(LocalDateTime s, LocalDateTime e);
    void deleteByTimestampBefore(LocalDateTime t);
}
