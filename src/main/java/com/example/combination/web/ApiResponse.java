package com.example.combination.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T>{
    private String message;
    private String error;
    private T data; //성공일 때만 담는 데이터
}
