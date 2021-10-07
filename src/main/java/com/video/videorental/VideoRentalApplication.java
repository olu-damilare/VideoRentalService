package com.video.videorental;

import com.video.videorental.data.VideoDto;
import com.video.videorental.data.videoType.NewRelease;
import com.video.videorental.services.VideoServicesImpl;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.video.videorental.data.Genre.*;

@SpringBootApplication
public class VideoRentalApplication {

	public static void main(String[] args) {

		SpringApplication.run(VideoRentalApplication.class, args);

	}



}
