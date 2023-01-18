package com.janghankyu.kokoro.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateRequest {
    @NotBlank(message = "{postUpdateRequest.title.notBlank}")
    private String title;

    @NotBlank(message = "{postUpdateRequest.content.notBlank}")
    private String content;

    @NotNull(message = "{postUpdateRequest.price.notNull}")
    @PositiveOrZero(message = "{postUpdateRequest.price.positiveOrZero}")
    private Long price;

    private List<MultipartFile> addedImages = new ArrayList<>();

    private List<Long> deletedImages = new ArrayList<>();
}
