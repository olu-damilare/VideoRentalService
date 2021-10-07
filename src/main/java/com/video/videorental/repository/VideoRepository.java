package com.video.videorental.repository;

import com.video.videorental.data.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends PagingAndSortingRepository<Video, Long> {

    Page<Video> findVideoByTitle(String title, Pageable pageableElements);
}
