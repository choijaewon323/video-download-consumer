package com.swmaestro.video.infra;

import com.swmaestro.video.domain.LocalVideo;
import com.swmaestro.video.domain.LocalVideoDownloader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Slf4j
@Component
public class YtdlpVideoDownloader implements LocalVideoDownloader {
    private static final int SUCCESS = 0;
    private static final File DOWNLOAD_DIRECTORY = new File("/download");

    @Override
    public LocalVideo downloadVideoToLocal(String youtubeUrl, String desiredName) {
        List<String> downloadCommand = getDownloadCommand(desiredName, youtubeUrl);
        return downloadProcess(downloadCommand, desiredName);
    }

    private LocalVideo downloadProcess(final List<String> downloadCommand, final String fileName) {
        final ProcessBuilder downloadBuilder = new ProcessBuilder(downloadCommand);
        downloadBuilder.directory(DOWNLOAD_DIRECTORY);
        downloadBuilder.redirectErrorStream(true);

        try {
            final Process process = downloadBuilder.start();

            readFromProcess(process);
            final int exitCode = process.waitFor();

            if (exitCode != SUCCESS) {
                throw new IllegalStateException("yt-dlp youtube 비디오 다운로드에 실패했습니다");
            }

            return findDownloadedVideo(fileName);
        } catch (IOException e) {
            throw new IllegalStateException("youtube 비디오 다운로드 중 IOException 발생 : " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("youtube 비디오 다운로드 중 InterruptedException 발생 : " + e.getMessage());
        }
    }

    private void readFromProcess(final Process process) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while (reader.readLine() != null) {}
    }

    private LocalVideo findDownloadedVideo(final String fileName) {
        final FilenameFilter filter = (dir, name) -> name.startsWith(fileName);
        final File[] files = DOWNLOAD_DIRECTORY.listFiles(filter);

        if (files == null || files.length == 0) {
            throw new IllegalStateException("다운로드 된 youtube 비디오 파일을 찾을 수 없습니다");
        }

        return new LocalVideo(files[0]);
    }

    private List<String> getDownloadCommand(String outputName, String youtubeUrl) {
        return List.of(
                "yt-dlp",
                "-o",
                outputName + ".%(ext)s",
                youtubeUrl
        );
    }
}
