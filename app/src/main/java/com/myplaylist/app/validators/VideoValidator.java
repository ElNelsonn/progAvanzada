package com.myplaylist.app.validators;


import com.myplaylist.app.repositories.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoValidator {

    private final VideoRepository videoRepository;


    public void validateUrlIsUnique(String url) {

        if (videoRepository.existsByUrl(url)) {
            throw new IllegalArgumentException("Url en uso.");
        }
    }







}
