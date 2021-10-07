package com.video.videorental.data;

import org.springframework.hateoas.RepresentationModel;
import com.video.videorental.data.videoType.VideoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto extends RepresentationModel<VideoDto>{
    private String title;
    private VideoType videoType;
    private String genre;


}
