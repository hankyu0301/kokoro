package com.janghankyu.kokoro.controller.post;

import com.janghankyu.kokoro.aop.AssignMemberId;
import com.janghankyu.kokoro.dto.post.PostCreateRequest;
import com.janghankyu.kokoro.dto.post.PostReadCondition;
import com.janghankyu.kokoro.dto.post.PostUpdateRequest;
import com.janghankyu.kokoro.dto.response.Response;
import com.janghankyu.kokoro.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping("/api/posts")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll(@Valid PostReadCondition cond) {
        return Response.success(postService.readAll(cond));
    }

    @PostMapping("/api/posts")
    @ResponseStatus(HttpStatus.CREATED)
    @AssignMemberId
    public Response create(@Valid @ModelAttribute PostCreateRequest req) {
        return Response.success(postService.create(req));
    }

    @GetMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(postService.read(id));
    }

    @DeleteMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        postService.delete(id);
        return Response.success();
    }

    @PutMapping("/api/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response update(
            @PathVariable Long id,
            @Valid @ModelAttribute PostUpdateRequest req) {
        return Response.success(postService.update(id, req));
    }
}
