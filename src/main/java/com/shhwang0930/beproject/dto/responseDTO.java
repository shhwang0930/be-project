package com.shhwang0930.beproject.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class responseDTO<T> {
    int status;
    T data;
}