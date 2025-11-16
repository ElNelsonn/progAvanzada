package com.myplaylist.app.repositories;

import com.myplaylist.app.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findAllByTitle(String title);

    Boolean existsByTitle(String title);

    Boolean existsByUrl(String url);



}
