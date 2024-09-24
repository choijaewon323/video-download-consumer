FROM linuxserver/ffmpeg:latest
WORKDIR /
RUN apt update && apt install -y locales
RUN locale-gen ko_KR.UTF-8
ENV LANG ko_KR.UTF-8
ENV LANGUAGE ko_KR.UTF-8
ENV LC_ALL ko_KR.UTF-8
RUN apt install -y openjdk-17-jdk
RUN apt install -y yt-dlp
RUN mkdir -p /download
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]