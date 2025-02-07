package com.example.beteyared.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private String message;
    private String recipientType;
    private String cohort;
    private Long userId;
}