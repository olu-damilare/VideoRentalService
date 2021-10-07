package com.video.videorental.services;


import com.video.videorental.data.PriceCalculatorDto;
import com.video.videorental.data.VideoDto;
import com.video.videorental.data.VideoDtoForPriceAndUsername;
import com.video.videorental.data.VideoSummaryDto;
import com.video.videorental.exceptions.VideoRentalException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.video.videorental.data.Genre.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class VideoServiceImplTest {

    @Autowired
    VideoServices videoServices;
    PriceCalculatorDto priceCalculatorDto;
    VideoSummaryDto videoSummaryDto;

    @BeforeEach
    void setUp(){
        videoSummaryDto = new VideoSummaryDto();
        priceCalculatorDto = new PriceCalculatorDto();
    }

    @AfterEach
    void tearDown(){
        videoSummaryDto = null;
        priceCalculatorDto = null;
    }

    @Test
    @Transactional
    void testToSaveAVideoToDB(){
        videoSummaryDto.setGenre(HORROR.toString());
        videoSummaryDto.setTitle("Evil Dead");
        videoSummaryDto.setVideoType("Children's Movie: 18");
        try {
            videoServices.saveVideo(videoSummaryDto);
        } catch (VideoRentalException e) {
            e.printStackTrace();
        }
        List<VideoSummaryDto> videoSummaryDtoList = videoServices.findAllVideos();
        VideoSummaryDto foundVideo = videoSummaryDtoList.get(0);
        log.info("The video found by title is ---> {}", foundVideo);
        log.info("The original videos in the db is ---> {}", videoSummaryDto);
        assertEquals(1, videoSummaryDtoList.size());
        assertEquals(videoSummaryDto, foundVideo);
    }


    @Test
    @Transactional
    void testToFetchAllVideosFromDB(){
        videoSummaryDto.setGenre(DRAMA.toString());
        videoSummaryDto.setTitle("Colon");
        videoSummaryDto.setVideoType("Regular");
        VideoSummaryDto secondVideoSummaryDto = new VideoSummaryDto();
        secondVideoSummaryDto.setVideoType("New Release: 2015");
        secondVideoSummaryDto.setTitle("Titanic");
        secondVideoSummaryDto.setGenre(ROMANCE.toString());

        try {
            videoServices.saveVideo(videoSummaryDto);
            videoServices.saveVideo(secondVideoSummaryDto);
        } catch (VideoRentalException e) {
            e.printStackTrace();
        }

        List<VideoSummaryDto> videoDtoList = videoServices.findAllVideos();
        log.info("The list of videos in the db are ---> {}", videoDtoList);
        assertFalse(videoDtoList.isEmpty());
        assertEquals(2, videoDtoList.size());
    }

    @Test
    @Transactional
    void testToCalculatePriceOfRegularVideoType(){
        videoSummaryDto.setGenre(DRAMA.toString());
        videoSummaryDto.setTitle("Colon");
        videoSummaryDto.setVideoType("Regular");
        try {
            videoServices.saveVideo(videoSummaryDto);
        } catch (VideoRentalException e) {
            e.printStackTrace();
        }
        priceCalculatorDto.setUsername("Damilare");
        priceCalculatorDto.setNumberOfDays(5);
        priceCalculatorDto.setSelectedVideoTitle("Colon");

        VideoDtoForPriceAndUsername videoDtoForPriceAndUsername = videoServices.calculatePrice(priceCalculatorDto);
        log.info("The video price dto is {}", videoDtoForPriceAndUsername);
        assertEquals(50, videoDtoForPriceAndUsername.getPrice());

    }

    @Test
    @Transactional
    void testToCalculatePriceOfChildrenMovieVideoType(){
        videoSummaryDto.setGenre(DRAMA.toString());
        videoSummaryDto.setTitle("Colon");
        videoSummaryDto.setVideoType("Children's Movie: 18");
        try {
            videoServices.saveVideo(videoSummaryDto);
        } catch (VideoRentalException e) {
            e.printStackTrace();
        }
        priceCalculatorDto.setUsername("Damilare");
        priceCalculatorDto.setNumberOfDays(5);
        priceCalculatorDto.setSelectedVideoTitle("Colon");

        VideoDtoForPriceAndUsername videoDtoForPriceAndUsername = videoServices.calculatePrice(priceCalculatorDto);
        log.info("The video price dto is {}", videoDtoForPriceAndUsername);
        assertEquals(49, videoDtoForPriceAndUsername.getPrice());

    }

//    @Test
//    void testToCalculateNewReleaseVideoType(){
//        videoSummaryDto.setGenre(DRAMA.toString());
//        videoSummaryDto.setTitle("Colon");
//        videoSummaryDto.setVideoType(new NewRelease(2010));
//        try {
//            videoServices.saveVideo(videoSummaryDto);
//        } catch (VideoRentalException e) {
//            e.printStackTrace();
//        }
//        priceCalculatorDto.setUsername("Damilare");
//        priceCalculatorDto.setNumberOfDays(5);
//        priceCalculatorDto.setSelectedVideoTitle("Colon");
//
//        VideoDtoForPriceAndUsername videoDtoForPriceAndUsername = videoServices.calculatePrice(priceCalculatorDto);
//        log.info("The video price dto is {}", videoDtoForPriceAndUsername);
//        assertEquals(49, videoDtoForPriceAndUsername.getPrice());
//
//    }

    @Test
    @Transactional
    void testToFindVideoByTitle(){
        videoSummaryDto.setGenre(ACTION.toString());
        videoSummaryDto.setTitle("Spectre");
        videoSummaryDto.setVideoType("New Release: 2010");
        try {
            videoServices.saveVideo(videoSummaryDto);
        } catch (VideoRentalException e) {
            e.printStackTrace();
        }
        VideoDto foundVideo = videoServices.findVideoByTitle(videoSummaryDto.getTitle());

        assertEquals(videoSummaryDto.getTitle(), foundVideo.getTitle());
        assertEquals(videoSummaryDto.getGenre(), foundVideo.getGenre());
        assertEquals(videoSummaryDto.getVideoType(), foundVideo.getVideoType().toString());
    }

    @Test
    void testThatInvalidMaximumAgeForChildrenMovie_throwsVideoRentalException(){
        videoSummaryDto.setGenre(DRAMA.toString());
        videoSummaryDto.setTitle("Colon");
        videoSummaryDto.setVideoType("Children's Movie: -1");
        assertThrows(VideoRentalException.class, ()-> videoServices.saveVideo(videoSummaryDto));
    }

    @Test
    void testThatReleaseYearLaterThanCurrentYearForNewRelease_throwsVideoRentalException(){
        videoSummaryDto.setGenre(DRAMA.toString());
        videoSummaryDto.setTitle("Colon");
        videoSummaryDto.setVideoType("New Release: 2022");
        assertThrows(VideoRentalException.class, ()-> videoServices.saveVideo(videoSummaryDto));
    }







}
