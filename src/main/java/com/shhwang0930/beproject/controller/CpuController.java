package com.shhwang0930.beproject.controller;

import com.shhwang0930.beproject.dto.responseDTO;
import com.shhwang0930.beproject.entity.CpuUsageDay;
import com.shhwang0930.beproject.entity.CpuUsageHour;
import com.shhwang0930.beproject.entity.CpuUsageMinute;
import com.shhwang0930.beproject.service.CpuService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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


    @ApiOperation(value = "분당 CPU 사용률 조회")
    @GetMapping("/minute")
    public responseDTO<List<CpuUsageMinute>> getCpuUsageMinute(@RequestParam("s") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime s,
                                                               @RequestParam("e") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime e){
        return new responseDTO<List<CpuUsageMinute>>(HttpStatus.OK.value(), cpuService.getCpuUsageMinute(s, e));
    }

    @ApiOperation(value = "시간당 CPU 사용률 조회")
    @GetMapping("/hour")
    public responseDTO<List<CpuUsageHour>> getCpuUsageHour(@RequestParam("s") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime s,
                                              @RequestParam("e") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime e){
        return new responseDTO<List<CpuUsageHour>>(HttpStatus.OK.value(), cpuService.getCpuUsageHour(s,e));
    }

    @ApiOperation(value = "일당 CPU 사용률 조회")
    @GetMapping("/day")
    public responseDTO<List<CpuUsageDay>> getCpuUsageDay(@RequestParam("s") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime s,
                                            @RequestParam("e") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime e){
        return new responseDTO<List<CpuUsageDay>>(HttpStatus.OK.value(), cpuService.getCpuUsageDay(s,e));
    }

}
