package com.swmaestro.video.domain.function;

import com.swmaestro.video.domain.dto.VideoDownloadMessageRequest;
import com.swmaestro.video.domain.dto.VideoDownloadMessageResponse;

@FunctionalInterface
public interface VideoDownloadFunction {
    VideoDownloadMessageResponse download(VideoDownloadMessageRequest request);
}
