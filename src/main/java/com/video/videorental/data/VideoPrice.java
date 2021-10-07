package com.video.videorental.data;

import com.video.videorental.data.videoType.VideoType;
import lombok.Data;

@Data
public class VideoPrice {
    private String videoTitle;
    private VideoType videoType;
    private String genre;
    private Double price;
}
