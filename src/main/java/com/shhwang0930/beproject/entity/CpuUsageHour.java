package com.shhwang0930.beproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "hours")
@Setter
@Getter
public class CpuUsageHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private double min;
    @Column
    private double max;
    @Column
    private double avr;
    @Column
    private LocalDateTime timestamp;
}
