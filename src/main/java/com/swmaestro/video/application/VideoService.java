package com.swmaestro.video.application;

import com.swmaestro.video.domain.*;
import com.swmaestro.video.domain.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoDownloader videoDownloader;
    private final VideoRepository videoRepository;
    private final IdGenerator idGenerator;

    public VideoDownloadMessageResponse download(VideoDownloadMessageRequest request) {
        log.info("video download start!");
        log.info("received url: {}", request.youtubeUrl());

        final long id = idGenerator.makeId();
        DownloadResult result = videoDownloader.download(request.youtubeUrl(), String.valueOf(id));
        String remoteUrl = videoRepository.save(result.localVideo());
        result.localVideo().delete();

        return VideoDownloadMessageResponse.builder()
                .videoId(request.videoId())
                .shortsId(request.shortsId())
                .s3Url(remoteUrl)
                .videoFullSecond(result.fullVideoSeconds())
                .originalTitle(result.originalTitle())
                .build();
    }
}
