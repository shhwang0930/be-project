package com.shhwang0930.beproject.controller;

import com.shhwang0930.beproject.service.CpuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class IntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CpuService cpuService;

    @Test
    @DisplayName("분당 사용률 조회")
    void getCpuUsageMinute() throws Exception {
        //given
        LocalDateTime startDateTime = LocalDateTime.now().minusMinutes(1);
        LocalDateTime endDateTime = LocalDateTime.now();

        //when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/cpu/minute")
                        .param("s", startDateTime.toString())
                        .param("e", endDateTime.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions.andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("시간당 사용률 조회")
    void getCpuUsageHour() throws Exception {
        //given
        LocalDateTime startDateTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endDateTime = LocalDateTime.now();

        //when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/cpu/minute")
                        .param("s", startDateTime.toString())
                        .param("e", endDateTime.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("일당 사용률 조회")
    void getCpuUsageDay() throws Exception {
        //given
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now();
        //when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/cpu/minute")
                        .param("s", startDateTime.toString())
                        .param("e", endDateTime.toString())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        actions.andExpect(status().isOk())
                .andDo(print());
    }
}
