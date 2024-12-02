package org.example.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdStatisticsResponse {
    private Long adId;
    private String title;
    private Integer views;
    private Double averagePrice;
}