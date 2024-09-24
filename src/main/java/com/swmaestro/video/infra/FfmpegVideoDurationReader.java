package com.swmaestro.video.infra;

import com.swmaestro.video.domain.LocalVideo;
import com.swmaestro.video.domain.VideoDurationReader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class FfmpegVideoDurationReader implements VideoDurationReader {

    @Override
    public int readDurationSeconds(LocalVideo localVideo) {
        List<String> durationCommand = getDurationCommand(localVideo.getPath());
        ProcessBuilder processBuilder = new ProcessBuilder(durationCommand);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            String duration = readDuration(process);
            process.waitFor();

            if (duration == null) {
                throw new IllegalStateException("동영상 전체 길이를 읽는데 실패했습니다");
            }

            return convertToInt(duration);
        } catch (IOException e) {
            throw new IllegalStateException("동영상 전체 길이 추출 중 IOException 발생");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("동영상 전체 길이 추출 중 InterruptedException 발생");
        }
    }

    private String readDuration(Process process) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String duration = null;
        while ((line = reader.readLine()) != null) {
            if (line.contains("Duration:")) {
                duration = line.split(",")[0].split("Duration: ")[1].trim();
            }
        }

        return duration;
    }

    // duration : 00:02:00.04
    private int convertToInt(String duration) {
        final int hourToSecond = 3600;
        final int minuteToSecond = 60;

        // hh:mm:ss
        String formattedDuration = duration.split("\\.")[0];
        String[] split = formattedDuration.split(":");

        final int hour = Integer.parseInt(split[0]);
        final int minute = Integer.parseInt(split[1]);
        final int second = Integer.parseInt(split[2]);

        return hour * hourToSecond + minute * minuteToSecond + second;
    }

    private List<String> getDurationCommand(String localVideoPath) {
        return List.of(
                "ffmpeg",
                "-i",
                localVideoPath,
                "-hide_banner"
        );
    }
}
