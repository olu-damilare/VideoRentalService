package com.video.videorental.data.videoType;

import com.video.videorental.data.Video;
import com.video.videorental.exceptions.VideoRentalException;
import lombok.Data;

@Data
public class ChildrenMovie extends VideoType{

    private final Double rate = 8.0;

    public ChildrenMovie(Integer maximumAge){
        if(maximumAge < 1) throw new VideoRentalException("Maximum age should not be less than 1");
        setData(String.valueOf(maximumAge));
    }

    @Override
    public String toString() {
        return "Children's Movie: " + getData();
    }
}
