package com.myplaylist.app.services;


import com.myplaylist.app.dtos.video.AddVideoDTO;
import com.myplaylist.app.dtos.video.VideoDTO;
import com.myplaylist.app.models.User;
import com.myplaylist.app.models.Video;
import com.myplaylist.app.repositories.UserRepository;
import com.myplaylist.app.repositories.VideoRepository;
import com.myplaylist.app.validators.VideoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final VideoValidator videoValidator;

    @Transactional
    public void addVideo(AddVideoDTO addVideoDTO) throws IllegalArgumentException {

        videoValidator.validateUrlIsUnique(addVideoDTO.getUrl());

        Video video = Video.builder().
                title(addVideoDTO.getTitle()).
                url(addVideoDTO.getUrl()).
                description(addVideoDTO.getDescription()).
                likes(0).
                build();

        videoRepository.save(video);
    }


    public List<VideoDTO> getAllVideos() {

          return videoRepository.findAll().stream()
                .map(v -> VideoDTO.builder()
                        .id(v.getId())
                        .title(v.getTitle())
                        .url(v.getUrl())
                        .description(v.getDescription())
                        .likes(v.getLikes())
                        .build()).toList();

    }

    @Transactional
    public void likeVideo(Long userId, Long videoId) throws IllegalArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video no encontrado."));

        if (user.getLikedVideoIds().contains(videoId)) {
            throw new IllegalArgumentException("Video ya ha sido likeado por el usuario.");
        }

        user.getLikedVideoIds().add(videoId);
        video.setLikes(video.getLikes() + 1);

        userRepository.save(user);
        videoRepository.save(video);
    }

    @Transactional
    public void unlikeVideo(Long userId, Long videoId) throws IllegalArgumentException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Video no encontrado."));

        if (!user.getLikedVideoIds().contains(videoId)) {
            throw new IllegalArgumentException("Video no fue sido likeado por el usuario.");
        }

        user.getLikedVideoIds().remove(Long.valueOf(videoId));
        video.setLikes(video.getLikes() - 1);

        userRepository.save(user);
        videoRepository.save(video);
    }




}
