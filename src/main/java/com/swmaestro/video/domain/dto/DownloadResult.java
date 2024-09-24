package com.swmaestro.video.domain.dto;

import com.swmaestro.video.domain.LocalVideo;
import lombok.Builder;

@Builder
public record DownloadResult(LocalVideo localVideo,
                             int fullVideoSeconds,
                             String originalTitle) {
}
