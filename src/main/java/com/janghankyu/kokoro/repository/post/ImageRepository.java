package com.janghankyu.kokoro.repository.post;

import com.janghankyu.kokoro.entity.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
