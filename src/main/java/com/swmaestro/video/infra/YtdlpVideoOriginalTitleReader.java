package com.swmaestro.video.infra;

import com.swmaestro.video.domain.VideoOriginalTitleReader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class YtdlpVideoOriginalTitleReader implements VideoOriginalTitleReader {
    @Override
    public String read(String youtubeUrl) {
        List<String> titleCommand = getTitleCommand(youtubeUrl);
        return titleProcess(titleCommand);
    }

    private String titleProcess(final List<String> titleCommand) {
        final ProcessBuilder processBuilder = new ProcessBuilder(titleCommand);
        processBuilder.redirectErrorStream(true);

        try {
            final Process process = processBuilder.start();

            final String originalTitle = readTitle(process);
            process.waitFor();

            return originalTitle;
        } catch (IOException e) {
            throw new IllegalStateException("youtube 원본 제목을 받는 중 IOException 발생 : " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("youtube 원본 제목을 받는 중 InterruptedException 발생 : " + e.getMessage());
        }
    }

    private String readTitle(final Process process) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        final String title = reader.readLine();

        if (title == null) {
            throw new IllegalStateException("youtube 원본 제목을 받는데 실패했습니다");
        }
        return title;
    }

    private List<String> getTitleCommand(String youtubeUrl) {
        return List.of(
                "yt-dlp",
                "-q",
                "--no-warnings",
                "--get-title",
                youtubeUrl
        );
    }
}
