package com.janghankyu.kokoro.service.post;

import com.janghankyu.kokoro.dto.post.*;
import com.janghankyu.kokoro.entity.category.Category;
import com.janghankyu.kokoro.entity.member.Member;
import com.janghankyu.kokoro.entity.post.Image;
import com.janghankyu.kokoro.entity.post.Post;
import com.janghankyu.kokoro.exception.CategoryNotFoundException;
import com.janghankyu.kokoro.exception.MemberNotFoundException;
import com.janghankyu.kokoro.exception.PostNotFoundException;
import com.janghankyu.kokoro.repository.category.CategoryRepository;
import com.janghankyu.kokoro.repository.member.MemberRepository;
import com.janghankyu.kokoro.repository.post.PostRepository;
import com.janghankyu.kokoro.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    public PostListDto readAll(PostReadCondition cond) {
        return PostListDto.toDto(
                postRepository.findAllByCondition(cond)
        );
    }

    @Transactional
    public PostCreateResponse create(PostCreateRequest req) {
        Member member = memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new);
        Category category = categoryRepository.findById(req.getCategoryId()).orElseThrow(CategoryNotFoundException::new);
        List<Image> images = req.getImages().stream().map(i -> new Image(i.getOriginalFilename())).collect(toList());

        Post post = postRepository.save(
                new Post(req.getTitle(), req.getContent(), req.getPrice(), member, category, images)
        );
        uploadImages(post.getImages(), req.getImages());
        return new PostCreateResponse(post.getId());
    }

    public PostDto read(Long id) {
        return PostDto.toDto(postRepository.findById(id).orElseThrow(PostNotFoundException::new));
    }

    @Transactional
    @PreAuthorize("@postGuard.check(#id)")
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        deleteImages(post.getImages());
        postRepository.delete(post);
    }

    @Transactional
    @PreAuthorize("@postGuard.check(#id)")
    public PostUpdateResponse update(Long id, PostUpdateRequest req) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        Post.ImageUpdatedResult result = post.update(req);
        uploadImages(result.getAddedImages(), result.getAddedImageFiles());
        deleteImages(result.getDeletedImages());
        return new PostUpdateResponse(id);
    }

    private void uploadImages(List<Image> images, List<MultipartFile> fileImages) {
        IntStream.range(0, images.size()).forEach(i -> fileService.upload(fileImages.get(i), images.get(i).getUniqueName()));
    }

    private void deleteImages(List<Image> images) {
        images.stream().forEach(i -> fileService.delete(i.getUniqueName()));
    }
}
