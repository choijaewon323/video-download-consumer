package com.swmaestro.video.domain;

import java.io.File;

public class LocalVideo {
    private final File localVideoFile;

    public LocalVideo(File localVideoFile) {
        this.localVideoFile = localVideoFile;
    }

    public String getPath() {
        return localVideoFile.getAbsolutePath();
    }

    public String getName() {
        return localVideoFile.getName();
    }

    public void delete() {
        final boolean isSuccessfullyDeleted = localVideoFile.delete();

        if (!isSuccessfullyDeleted) {
            throw new IllegalStateException("임시 비디오 파일 삭제에 실패했습니다");
        }
    }
}
