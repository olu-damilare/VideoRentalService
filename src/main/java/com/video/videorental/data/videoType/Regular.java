package com.video.videorental.data.videoType;

import lombok.Data;

@Data
public class Regular extends VideoType{

    private final Double rate = 10.0;


    @Override
    public String toString() {
        return "Regular";
    }
}
