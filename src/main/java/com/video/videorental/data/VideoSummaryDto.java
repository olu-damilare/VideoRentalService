package com.video.videorental.data;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class VideoSummaryDto extends RepresentationModel<VideoSummaryDto> {
    private String title;
    private String videoType;
    private String genre;
}
