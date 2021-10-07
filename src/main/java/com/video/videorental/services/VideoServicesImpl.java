package com.video.videorental.services;

import com.video.videorental.data.*;
import com.video.videorental.data.videoType.*;
import com.video.videorental.data.videoType.VideoType;
import com.video.videorental.exceptions.VideoRentalException;
import com.video.videorental.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoServicesImpl implements VideoServices{
    @Autowired
    VideoRepository videoRepository;

    @Autowired
    ModelMapper modelMapper = new ModelMapper();

    private final int paginationSize = 10;

//    @Override
//    public List<VideoDto> findAllVideos() {
//        Pageable pageableElements = PageRequest.of(0, paginationSize);
//        Page<Video> videos = videoRepository.findAll(pageableElements);
//
//        return videos.stream()
//                                            .map(video -> {
//                                                VideoType videoType = null;
//                                                String videoTypeData = video.getVideoType();
//                                                if(videoTypeData.contains("Regular")){
//                                                    videoType = new Regular();
//                                                }else if(videoTypeData.contains("Children's Movie")){
//                                                    videoType = getVideoTypeObject(videoTypeData);
//                                                }else if(videoTypeData.contains("New Release")) {
//                                                    videoType = getVideoTypeObject(videoTypeData);
//                                                }
//                                                return new VideoDto(video.getTitle(), videoType, video.getGenre());
//                                            })
//                                            .collect(Collectors.toList());
//
//    }

    @Override
    public List<VideoSummaryDto> findAllVideos() {
        Pageable pageableElements = PageRequest.of(0, paginationSize);
        Page<Video> videos = videoRepository.findAll(pageableElements);

        return videos.stream()
                .map(video -> {
                    VideoSummaryDto videoSummary = new VideoSummaryDto();
                    modelMapper.map(video, videoSummary);
                    return videoSummary;
                })
                .collect(Collectors.toList());

    }

    private VideoType getVideoTypeObject(String videoTypeData) {

        if(videoTypeData.contains("Regular")){
            return new Regular();
        }
        int data = extractData(videoTypeData);
        if(videoTypeData.contains("Children's Movie")) {
            return new ChildrenMovie(data);
        }else if(videoTypeData.contains("New Release")){
            return new NewRelease(data);
        }
        throw new VideoRentalException("Invalid video type");
    }

    private int extractData(String videoTypeData) {
        String[] data = videoTypeData.split(":");
        int value = Integer.parseInt(data[1].trim());

        if(data[0].contains("Children's Movie") && value < 1){
            throw new VideoRentalException("Maximum age should not be less than 1");
        }

        int currentYear = LocalDate.now().getYear();
        if(data[0].contains("New Release") && currentYear < value) throw new VideoRentalException("Year of release should not be later than current year");


        return value;
    }

    @Override
    public VideoSummaryDto saveVideo(VideoSummaryDto videoSummaryDto) throws VideoRentalException {
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoType(getVideoTypeObject(videoSummaryDto.getVideoType()));
        validateVideoToSave(videoSummaryDto);
        Video video = Video.builder()
                            .genre(videoSummaryDto.getGenre())
                            .videoType(videoSummaryDto.getVideoType())
                            .title(videoSummaryDto.getTitle())
                            .rate(videoDto.getVideoType().getRate())
                            .build();

        videoRepository.save(video);
        return videoSummaryDto;
    }

    private void validateVideoToSave(VideoSummaryDto video) {
        boolean invalidVideoGenre = video.getGenre() == null || video.getGenre().isBlank() || video.getGenre().isEmpty();
        boolean invalidVideoType = video.getVideoType() == null || video.getVideoType().isBlank() || video.getVideoType().isEmpty();

        if(invalidVideoGenre){
            throw new VideoRentalException("Video genre should not be empty");
        }
        if(invalidVideoType){
            throw new VideoRentalException("Video type should not be empty");
        }
        if(video.getTitle() == null || video.getTitle().isEmpty() || video.getTitle().isBlank()){
            throw new VideoRentalException("Video title should not be empty");
        }

        if(!isValidVideoGenre(video.getGenre())){
            throw new VideoRentalException("Invalid video genre");
        }
    }


    private boolean isValidVideoGenre(String genre) {
        log.info("video genre is --> {}", genre);
        for(Genre g: Genre.values()){
            if(g.toString().equalsIgnoreCase(genre)){
                return true;
            }
        }
        return false;
    }


    @Override
    public VideoDtoForPriceAndUsername calculatePrice(PriceCalculatorDto priceCalculatorDto){
        validatePriceCalculatorDto(priceCalculatorDto);

        VideoSummaryDto videoDto = findAllVideos().stream()
                                        .filter(selectVideo -> selectVideo.getTitle()
                                                .equalsIgnoreCase(priceCalculatorDto.getSelectedVideoTitle()))
                                                .findFirst().orElseThrow(() -> new VideoRentalException("Video with the provided title is unavailable"));

        return getVideoPrice(priceCalculatorDto, videoDto);
    }

    private VideoDtoForPriceAndUsername getVideoPrice(PriceCalculatorDto priceCalculatorDto, VideoSummaryDto videoDto) {
        VideoDtoForPriceAndUsername videoPrice = new VideoDtoForPriceAndUsername();

        modelMapper.map(videoDto, videoPrice);
        VideoType videoType = getVideoTypeObject(videoDto.getVideoType());
        Double price = calculatePriceForVideo(videoType, priceCalculatorDto);
        videoPrice.setPrice(price);
        videoPrice.setUsername(priceCalculatorDto.getUsername());
        videoPrice.setNumberOfDays(priceCalculatorDto.getNumberOfDays());
        videoPrice.setVideoType(videoType.toString());
        return videoPrice;
    }

    private void validatePriceCalculatorDto(PriceCalculatorDto priceCalculatorDto) {
        if(priceCalculatorDto.getNumberOfDays() < 1){
            throw new VideoRentalException("Invalid number of days");
        }
        if(priceCalculatorDto.getUsername() == null || priceCalculatorDto.getUsername().isEmpty() || priceCalculatorDto.getUsername().isBlank()){
            throw new VideoRentalException("Username should not be empty");
        }
    }

    private Double calculatePriceForVideo(VideoType videoType, PriceCalculatorDto priceCalculatorDto) {
        Double price = 0.0;
        if(videoType instanceof Regular){
            log.info("the rate is {}", videoType.getRate());
            price = videoType.getRate() * priceCalculatorDto.getNumberOfDays();
        }else if(videoType instanceof ChildrenMovie){
            price = videoType.getRate() * priceCalculatorDto.getNumberOfDays() + Integer.parseInt(videoType.getData()) / 2 ;
        }else if(videoType instanceof NewRelease){
            price = videoType.getRate() * priceCalculatorDto.getNumberOfDays() - Integer.parseInt(videoType.getData());
        }
        return price;
    }

    @Override
    public VideoDto findVideoByTitle(String title) {
        Pageable pageableElements = PageRequest.of(0, paginationSize);
        Video video =  videoRepository.findVideoByTitle(title, pageableElements)
                                .stream()
                                .findFirst()
                                .orElseThrow(() -> new VideoRentalException("Video with the selected title is unavailable"));

        VideoDto videoDto = new VideoDto();
        modelMapper.map(video, videoDto);
        videoDto.setVideoType(getVideoTypeObject(video.getVideoType()));

        return videoDto;
    }

    @Override
    public VideoPriceDto findVideoWithPriceByTitle(String title){
        VideoDto videoDto = findVideoByTitle(title);
        VideoPriceDto videoPriceDto = new VideoPriceDto();
        modelMapper.map(videoDto, videoPriceDto);
        videoPriceDto.setRate(videoDto.getVideoType().getRate());
        videoPriceDto.setVideoType(videoDto.getVideoType().toString());

        return videoPriceDto;
    }

//    @Override
//    public VideoPrice findVideoById(Long id){
//        return null;
//    }
//
  }
