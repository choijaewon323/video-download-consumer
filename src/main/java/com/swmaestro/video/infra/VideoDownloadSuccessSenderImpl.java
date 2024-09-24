package com.swmaestro.video.infra;

import com.swmaestro.video.domain.VideoDownloadSuccessSender;
import com.swmaestro.video.domain.dto.VideoDownloadMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class VideoDownloadSuccessSenderImpl implements VideoDownloadSuccessSender {
    @Value("${test-url}")
    private String URL;

    @Override
    public void send(VideoDownloadMessageResponse response) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<VideoDownloadMessageResponse> request = new HttpEntity<>(response, headers);

        restTemplate.postForEntity(URL, request, Void.class);
    }
}
