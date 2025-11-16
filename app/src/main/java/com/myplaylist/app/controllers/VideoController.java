package com.myplaylist.app.controllers;


import com.myplaylist.app.dtos.video.AddVideoDTO;
import com.myplaylist.app.dtos.video.VideoDTO;
import com.myplaylist.app.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;


    @PostMapping("")
    public ResponseEntity<String> addVideo(@RequestBody AddVideoDTO addVideoDTO) {

        try {

            videoService.addVideo(addVideoDTO);
            return ResponseEntity.ok("Video added successfully");

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(),  HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<VideoDTO>> getAllVideos() {

        return ResponseEntity.ok(videoService.getAllVideos());
    }

    @PostMapping("/like/{id}/{videoId}")
    public ResponseEntity<String> likeVideo(@PathVariable Long id, @PathVariable Long videoId) {

        try {

            videoService.likeVideo(id, videoId);
            return ResponseEntity.ok("Video liked successfully");

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(),  HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/unlike/{id}/{videoId}")
    public ResponseEntity<String> unlikeVideo(@PathVariable Long id, @PathVariable Long videoId) {

        try {

            videoService.unlikeVideo(id, videoId);
            return ResponseEntity.ok("Video unliked successfully");

        } catch (IllegalArgumentException e) {

            return new ResponseEntity<>(e.getMessage(),  HttpStatus.CONFLICT);
        }
    }

    




}
