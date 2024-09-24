package com.swmaestro.video.domain.function;

import com.swmaestro.video.domain.dto.VideoDownloadMessageResponse;

@FunctionalInterface
public interface AfterVideoDownloadSuccessFunction {
    void sendSuccess(VideoDownloadMessageResponse response);
}
