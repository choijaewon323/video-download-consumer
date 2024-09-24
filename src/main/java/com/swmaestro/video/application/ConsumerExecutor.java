package com.swmaestro.video.application;

import com.swmaestro.video.domain.VideoDownloadConsumer;
import com.swmaestro.video.domain.VideoDownloadSuccessSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ConsumerExecutor implements ApplicationRunner {
    private final VideoService videoService;
    private final VideoDownloadSuccessSender successSender;
    private final VideoDownloadConsumer consumer;

    @Override
    public void run(ApplicationArguments args) {
        /*
        log.info("ConsumerExecutor start!");
        consumer.consume(videoService::download, successSender::send);

         */
    }
}
