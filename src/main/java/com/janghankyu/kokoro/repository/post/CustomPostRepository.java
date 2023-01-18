package com.janghankyu.kokoro.repository.post;

import com.janghankyu.kokoro.dto.post.PostReadCondition;
import com.janghankyu.kokoro.dto.post.PostSimpleDto;
import org.springframework.data.domain.Page;

public interface CustomPostRepository {
    Page<PostSimpleDto> findAllByCondition(PostReadCondition cond);
}
