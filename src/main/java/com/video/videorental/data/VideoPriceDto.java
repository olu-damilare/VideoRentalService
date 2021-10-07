package com.video.videorental.data;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class VideoPriceDto extends RepresentationModel<VideoPriceDto> {
    private String title;
    private String videoType;
    private String genre;
    private Double rate;
}
