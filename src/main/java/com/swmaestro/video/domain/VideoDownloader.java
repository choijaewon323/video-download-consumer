package com.swmaestro.video.domain;

import com.swmaestro.video.domain.dto.DownloadResult;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class VideoDownloader {
    private final LocalVideoDownloader localVideoDownloader;
    private final VideoDurationReader videoDurationReader;
    private final VideoOriginalTitleReader videoOriginalTitleReader;

    public DownloadResult download(String youtubeUrl, String desiredName) {
        LocalVideo localVideo = localVideoDownloader.downloadVideoToLocal(youtubeUrl, desiredName);
        String originalTitle = videoOriginalTitleReader.read(youtubeUrl);
        String emojiRemovedOriginalTitle = EmojiParser.removeAllEmojis(originalTitle);
        int fullVideoSeconds = videoDurationReader.readDurationSeconds(localVideo);

        return DownloadResult.builder()
                .localVideo(localVideo)
                .fullVideoSeconds(fullVideoSeconds)
                .originalTitle(emojiRemovedOriginalTitle)
                .build();
    }
}
