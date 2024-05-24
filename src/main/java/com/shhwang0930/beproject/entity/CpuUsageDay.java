package com.shhwang0930.beproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "days")
@Setter
@Getter
public class CpuUsageDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double min;
    private double max;
    private double avr;

    private LocalDateTime timestamp;
}
