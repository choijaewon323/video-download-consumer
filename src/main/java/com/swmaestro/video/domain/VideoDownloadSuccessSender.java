package com.swmaestro.video.domain;

import com.swmaestro.video.domain.dto.VideoDownloadMessageResponse;

public interface VideoDownloadSuccessSender {
    void send(VideoDownloadMessageResponse response);
}
