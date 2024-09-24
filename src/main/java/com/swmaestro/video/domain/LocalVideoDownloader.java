package com.swmaestro.video.domain;

public interface LocalVideoDownloader {
    LocalVideo downloadVideoToLocal(String youtubeUrl, String desiredName);
}
