package com.janghankyu.kokoro.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "{postCreateRequest.title.notBlank}")
    private String title;

    @NotBlank(message = "{postCreateRequest.content.notBlank}")
    private String content;

    @NotNull(message = "{postCreateRequest.price.notNull")
    @PositiveOrZero(message = "{postCreateRequest.price.positiveOrZero}")
    private Long price;

    @Null
    private Long memberId;

    @NotNull(message = "{postCreateRequest.categoryId.notNull}")
    @PositiveOrZero(message = "{postCreateRequest.categoryId.positiveOrZero}")
    private Long categoryId;

    private List<MultipartFile> images = new ArrayList<>();
}
