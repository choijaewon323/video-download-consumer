package com.swmaestro.video.domain.dto;

import lombok.Builder;

@Builder
public record VideoDownloadMessageRequest(long videoId,
                                          long shortsId,
                                          String youtubeUrl) {
}
