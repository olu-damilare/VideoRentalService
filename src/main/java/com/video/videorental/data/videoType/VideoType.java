package com.video.videorental.data.videoType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class VideoType {

    private String data;
    private Double rate;

}
