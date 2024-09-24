package com.swmaestro.video.api;

import com.swmaestro.video.application.VideoService;
import com.swmaestro.video.domain.dto.VideoDownloadMessageRequest;
import com.swmaestro.video.domain.dto.VideoDownloadMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/video/download")
    public VideoDownloadMessageResponse downloadVideo(@RequestBody VideoDownloadMessageRequest request) {
        return videoService.download(request);
    }
}
