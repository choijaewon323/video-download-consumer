package com.swmaestro.video.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.swmaestro.video.domain.VideoDownloadConsumer;
import com.swmaestro.video.domain.function.AfterVideoDownloadSuccessFunction;
import com.swmaestro.video.domain.function.VideoDownloadFunction;
import com.swmaestro.video.domain.dto.VideoDownloadMessageRequest;
import com.swmaestro.video.domain.dto.VideoDownloadMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RabbitMQVideoDownloadConsumer implements VideoDownloadConsumer {
    private static final String QUEUE_NAME = "video-download";

    private final Connection connection;
    private final ObjectMapper objectMapper;

    @Override
    public void consume(VideoDownloadFunction videoDownloadFunction, AfterVideoDownloadSuccessFunction afterSuccessFunction) {
        try {
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicQos(1);

            channel.basicConsume(QUEUE_NAME, false, (consumerTag, delivery) -> {
                try {
                    VideoDownloadMessageRequest request = objectMapper.readValue(delivery.getBody(), VideoDownloadMessageRequest.class);

                    log.info("received : {}", request.videoId());
                    log.info("received : {}", request.shortsId());
                    log.info("received : {}", request.youtubeUrl());

                    VideoDownloadMessageResponse response = videoDownloadFunction.download(request);

                    afterSuccessFunction.sendSuccess(response);

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    throw new IllegalStateException("consumer 내부 문제 발생 : " + e.getMessage());
                }
            }, consumerTag -> {});
        } catch (Exception e) {
            throw new IllegalStateException("video download consume에 실패했습니다 : " + e.getMessage());
        }
    }
}
