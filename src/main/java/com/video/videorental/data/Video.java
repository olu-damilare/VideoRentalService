package com.video.videorental.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long videoId;
    @Column
    private String title;
    @Column
    private String videoType;
    @Column
    private String genre;
    @Column
    private Double rate;
}
