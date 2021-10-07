package com.video.videorental.controller;


import com.video.videorental.data.*;
import com.video.videorental.exceptions.VideoRentalException;
import com.video.videorental.services.VideoServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("")
public class VideoController {

    @Autowired
    VideoServices videoServices;

    @PostMapping("/save")
    public ResponseEntity<?> saveVideo(@RequestBody VideoSummaryDto videoSummaryDtoDto){
        VideoSummaryDto savedVideo;
        try {
            savedVideo = videoServices.saveVideo(videoSummaryDtoDto);
        }catch(VideoRentalException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(savedVideo, HttpStatus.CREATED);
    }

    @GetMapping("/videos")
    public ResponseEntity<?> findAllVideos(){
        List<VideoSummaryDto> videos = videoServices.findAllVideos()
            .stream()
            .map(videoDto -> {
                Link priceLink = linkTo(methodOn(VideoController.class).getVideoWithPrice(videoDto.getTitle())).withRel("Video info");
              return  videoDto.add(priceLink
//                    linkTo(methodOn(VideoController.class).findAllVideos()).withSelfRel()
                );
            }).collect(Collectors.toList()
            );

        return new ResponseEntity<>(videos, HttpStatus.OK);

    }

    @GetMapping("/videos/{title}")
    public ResponseEntity<?> getVideoWithPrice(@PathVariable("title") String title) {
        VideoPriceDto videoPriceDto;

        try{
             videoPriceDto = videoServices.findVideoWithPriceByTitle(title);
             Link allVideosLink = linkTo(methodOn(VideoController.class)
                     .findAllVideos()).withRel("All videos");
             videoPriceDto.add(allVideosLink);
             return new ResponseEntity<>(videoPriceDto, HttpStatus.OK);
        }catch(VideoRentalException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/videos/price")
    public ResponseEntity<?> getVideoPrice(@RequestBody PriceCalculatorDto priceCalculatorDto){
        VideoDtoForPriceAndUsername videoDtoForPriceAndUsername;
        try{
            videoDtoForPriceAndUsername = videoServices.calculatePrice(priceCalculatorDto);
            return new ResponseEntity<>(videoDtoForPriceAndUsername, HttpStatus.OK);
        }catch (VideoRentalException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
