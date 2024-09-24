package com.swmaestro.video.domain;

import com.swmaestro.video.domain.function.AfterVideoDownloadSuccessFunction;
import com.swmaestro.video.domain.function.VideoDownloadFunction;

public interface VideoDownloadConsumer {
    void consume(VideoDownloadFunction videoDownloadFunction, AfterVideoDownloadSuccessFunction afterSuccessFunction);
}
