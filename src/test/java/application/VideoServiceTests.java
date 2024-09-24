package application;

import com.swmaestro.video.application.VideoService;
import com.swmaestro.video.domain.*;
import com.swmaestro.video.domain.dto.VideoDownloadMessageRequest;
import com.swmaestro.video.domain.dto.VideoDownloadMessageResponse;
import com.swmaestro.video.infra.TsidIdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class VideoServiceTests {
    VideoDownloader videoDownloader = new VideoDownloader(new MockLocalVideoDownloader(),
            new MockVideoDurationReader(),
            new MockVideoOriginalTitleReader());
    VideoRepository videoRepository = new MockVideoRepository();
    IdGenerator idGenerator = new TsidIdGenerator();
    VideoService videoService = new VideoService(videoDownloader, videoRepository, idGenerator);

    static final String EXPECTED_ORIGINAL_TITLE = "원본 youtube 제목";
    static final int EXPECTED_DURATION_SECONDS = 1000;
    static final String EXPECTED_REMOTE_URL = "remote url";

    @DisplayName("video download 로직 테스트")
    @Test
    void downloadTest() {
        // given
        VideoDownloadMessageRequest request = VideoDownloadMessageRequest.builder()
                .videoId(0L)
                .shortsId(0L)
                .youtubeUrl("test url")
                .build();

        // when
        VideoDownloadMessageResponse response = videoService.download(request);

        // then
        assertThat(response.originalTitle()).isEqualTo(EXPECTED_ORIGINAL_TITLE);
        assertThat(response.videoFullSecond()).isEqualTo(EXPECTED_DURATION_SECONDS);
        assertThat(response.s3Url()).isEqualTo(EXPECTED_REMOTE_URL);
    }

    private static class MockVideoRepository implements VideoRepository {
        private final List<String> videos = new ArrayList<>();

        @Override
        public String save(LocalVideo localVideo) {
            videos.add(localVideo.getPath());
            return EXPECTED_REMOTE_URL;
        }

        List<String> findAll() {
            return videos;
        }
    }

    private static class MockLocalVideoDownloader implements LocalVideoDownloader {
        @Override
        public LocalVideo downloadVideoToLocal(String youtubeUrl, String desiredName) {
            try {
                File localVideoFile = File.createTempFile("test", ".mp4");
                return new LocalVideo(localVideoFile);
            } catch (Exception ignored) {
                throw new RuntimeException("local video 파일 생성 실패");
            }
        }
    }

    private static class MockVideoDurationReader implements VideoDurationReader {
        @Override
        public int readDurationSeconds(LocalVideo localVideo) {
            return EXPECTED_DURATION_SECONDS;
        }
    }

    private static class MockVideoOriginalTitleReader implements VideoOriginalTitleReader {
        @Override
        public String read(String youtubeUrl) {
            return EXPECTED_ORIGINAL_TITLE;
        }
    }
}
