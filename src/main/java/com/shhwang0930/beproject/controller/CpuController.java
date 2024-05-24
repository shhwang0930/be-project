package com.shhwang0930.beproject.controller;

import com.shhwang0930.beproject.entity.CpuUsageDay;
import com.shhwang0930.beproject.entity.CpuUsageHour;
import com.shhwang0930.beproject.entity.CpuUsageMinute;
import com.shhwang0930.beproject.service.CpuService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cpu")
@RequiredArgsConstructor
public class CpuController {

    private final CpuService cpuService;


    @GetMapping("/minute")
    public List<CpuUsageMinute> getCpuUsageMinute(@RequestParam("s") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime s,
                                                  @RequestParam("e") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime e){
        return cpuService.getCpuUsageMinute(s, e);
    }

    @GetMapping("/hour")
    public List<CpuUsageHour> getCpuUsageHour(@RequestParam("s") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime s,
                                              @RequestParam("e") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime e){
        return cpuService.getCpuUsageHour(s,e);
    }
    @GetMapping("/day")
    public List<CpuUsageDay> getCpuUsageDay(@RequestParam("s") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime s,
                                            @RequestParam("e") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime e){
        return cpuService.getCpuUsageDay(s,e);
    }

}
