package com.myplaylist.app;

import com.myplaylist.app.dtos.video.AddVideoDTO;
import com.myplaylist.app.dtos.video.VideoDTO;
import com.myplaylist.app.models.User;
import com.myplaylist.app.models.Video;
import com.myplaylist.app.repositories.UserRepository;
import com.myplaylist.app.repositories.VideoRepository;
import com.myplaylist.app.services.VideoService;
import com.myplaylist.app.validators.VideoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VideoValidator videoValidator;

    @InjectMocks
    private VideoService videoService;

    @Test
    void addVideo_Success() {
        AddVideoDTO dto = AddVideoDTO.builder()
                .title("Test Video")
                .url("https://youtube.com/test")
                .description("Test description")
                .build();

        doNothing().when(videoValidator).validateUrlIsUnique(dto.getUrl());
        when(videoRepository.save(any(Video.class))).thenReturn(new Video());

        assertDoesNotThrow(() -> videoService.addVideo(dto));
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    void addVideo_UrlExists_ThrowsException() {
        AddVideoDTO dto = AddVideoDTO.builder()
                .title("Test Video")
                .url("https://youtube.com/existing")
                .description("Test description")
                .build();

        doThrow(new IllegalArgumentException("Video URL already exists"))
                .when(videoValidator).validateUrlIsUnique(dto.getUrl());

        assertThrows(IllegalArgumentException.class, () -> videoService.addVideo(dto));
        verify(videoRepository, never()).save(any(Video.class));
    }

    @Test
    void getAllVideos_ReturnsListOfVideos() {
        List<Video> videos = Arrays.asList(
                Video.builder().id(1L).title("Video 1").url("url1").likes(5).build(),
                Video.builder().id(2L).title("Video 2").url("url2").likes(10).build()
        );

        when(videoRepository.findAll()).thenReturn(videos);

        List<VideoDTO> result = videoService.getAllVideos();

        assertEquals(2, result.size());
        assertEquals("Video 1", result.get(0).getTitle());
        assertEquals("Video 2", result.get(1).getTitle());
    }

    @Test
    void likeVideo_Success() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .likedVideoIds(new ArrayList<>())
                .build();

        Video video = Video.builder()
                .id(1L)
                .title("Test Video")
                .likes(0)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        assertDoesNotThrow(() -> videoService.likeVideo(1L, 1L));

        assertTrue(user.getLikedVideoIds().contains(1L));
        assertEquals(1, video.getLikes());
        verify(userRepository, times(1)).save(user);
        verify(videoRepository, times(1)).save(video);
    }

    @Test
    void likeVideo_AlreadyLiked_ThrowsException() {
        User user = User.builder()
                .id(1L)
                .likedVideoIds(new ArrayList<>(List.of(1L)))
                .build();

        Video video = Video.builder().id(1L).likes(1).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        assertThrows(IllegalArgumentException.class, () -> videoService.likeVideo(1L, 1L));
    }

    @Test
    void unlikeVideo_Success() {
        User user = User.builder()
                .id(1L)
                .likedVideoIds(new ArrayList<>(List.of(1L)))
                .build();

        Video video = Video.builder()
                .id(1L)
                .likes(1)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        assertDoesNotThrow(() -> videoService.unlikeVideo(1L, 1L));

        assertFalse(user.getLikedVideoIds().contains(1L));
        assertEquals(0, video.getLikes());
    }

    @Test
    void unlikeVideo_NotLiked_ThrowsException() {
        User user = User.builder()
                .id(1L)
                .likedVideoIds(new ArrayList<>())
                .build();

        Video video = Video.builder().id(1L).likes(0).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        assertThrows(IllegalArgumentException.class, () -> videoService.unlikeVideo(1L, 1L));
    }
}