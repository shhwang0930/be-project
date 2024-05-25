package com.shhwang0930.beproject.entity;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity(name = "minutes")
@Setter
@Getter
public class CpuUsageMinute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "`usage`")
    private double usage;
    @Column
    private LocalDateTime timestamp;
}
