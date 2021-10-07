package com.video.videorental.data;

import lombok.Data;

@Data
public class PriceCalculatorDto {
    private String username;
    private String selectedVideoTitle;
    private Integer numberOfDays;
}
