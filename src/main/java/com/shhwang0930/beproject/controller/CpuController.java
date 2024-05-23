package com.shhwang0930.beproject.controller;

import com.shhwang0930.beproject.service.CpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cpu")
@RequiredArgsConstructor
public class CpuController {

    private final CpuService cpuService;

    @GetMapping("/minute")
    public void getCpuUsageMinute(){
        cpuService.checkCpuUsage();
    }
}
