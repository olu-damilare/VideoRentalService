package com.video.videorental.services;

import com.video.videorental.data.*;
import com.video.videorental.exceptions.VideoRentalException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VideoServices {

    VideoSummaryDto saveVideo(VideoSummaryDto videoDto) throws VideoRentalException;
    VideoDtoForPriceAndUsername calculatePrice(PriceCalculatorDto priceCalculatorDto);
    VideoPriceDto findVideoWithPriceByTitle(String title);
    VideoDto findVideoByTitle(String selectedVideoTitle);
    List<VideoSummaryDto> findAllVideos();
}
