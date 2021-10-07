package com.video.videorental.data.videoType;

import com.video.videorental.exceptions.VideoRentalException;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewRelease extends VideoType{

    private final Double rate = 15.0;

    public NewRelease(Integer yearOfRelease){
        int currentYear = LocalDate.now().getYear();
        if(currentYear < yearOfRelease) throw new VideoRentalException("Year of release should not be later than current year");

        setData(String.valueOf(yearOfRelease));
    }

    @Override
    public String toString() {
        return "New Release: " + getData();
    }
}
