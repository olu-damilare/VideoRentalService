package com.video.videorental.data;

import com.video.videorental.data.videoType.VideoType;
import lombok.Data;

@Data
public class VideoDtoForPriceAndUsername {
    private String username;
    private String title;
    private Integer numberOfDays;
    private Double price;
    private String videoType;
}
