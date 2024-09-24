package com.swmaestro.video.infra;

import com.swmaestro.video.domain.LocalVideo;
import com.swmaestro.video.domain.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

@RequiredArgsConstructor
@Component
public class S3VideoRepository implements VideoRepository {
    private final S3Client s3Client;

    @Value("${cloud.aws.bucket-name}")
    private String bucketName;
    @Value("${cloud.aws.s3-url}")
    private String s3Url;

    @Override
    public String save(LocalVideo localVideo) {
        String keyName = makeKeyName(localVideo.getName());

        final PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();
        s3Client.putObject(objectRequest, RequestBody.fromFile(Path.of(localVideo.getPath())));

        return getRemoteS3Url(keyName);
    }

    private String makeKeyName(String videoName) {
        return "origin/" + videoName;
    }

    private String getRemoteS3Url(String keyName) {
        return s3Url + keyName;
    }
}
