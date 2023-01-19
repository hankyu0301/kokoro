package com.janghankyu.kokoro.service.post;

import com.janghankyu.kokoro.dto.post.PostCreateRequest;
import com.janghankyu.kokoro.dto.post.PostDto;
import com.janghankyu.kokoro.dto.post.PostListDto;
import com.janghankyu.kokoro.dto.post.PostUpdateRequest;
import com.janghankyu.kokoro.entity.post.Image;
import com.janghankyu.kokoro.entity.post.Post;
import com.janghankyu.kokoro.exception.CategoryNotFoundException;
import com.janghankyu.kokoro.exception.MemberNotFoundException;
import com.janghankyu.kokoro.exception.PostNotFoundException;
import com.janghankyu.kokoro.exception.UnsupportedImageFormatException;
import com.janghankyu.kokoro.repository.category.CategoryRepository;
import com.janghankyu.kokoro.repository.member.MemberRepository;
import com.janghankyu.kokoro.repository.post.PostRepository;
import com.janghankyu.kokoro.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.janghankyu.kokoro.factory.dto.PostCreateRequestFactory.createPostCreateRequest;
import static com.janghankyu.kokoro.factory.dto.PostCreateRequestFactory.createPostCreateRequestWithImages;
import static com.janghankyu.kokoro.factory.dto.PostReadConditionFactory.*;
import static com.janghankyu.kokoro.factory.dto.PostUpdateRequestFactory.createPostUpdateRequest;
import static com.janghankyu.kokoro.factory.entity.CategoryFactory.createCategory;
import static com.janghankyu.kokoro.factory.entity.ImageFactory.*;
import static com.janghankyu.kokoro.factory.entity.ImageFactory.createImage;
import static com.janghankyu.kokoro.factory.entity.MemberFactory.createMember;
import static com.janghankyu.kokoro.factory.entity.PostFactory.createPostWithImages;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks PostService postService;
    @Mock PostRepository postRepository;
    @Mock MemberRepository memberRepository;
    @Mock CategoryRepository categoryRepository;
    @Mock FileService fileService;

    @Test
    void createTest() {
        // given
        PostCreateRequest req = createPostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));
        given(postRepository.save(any())).willReturn(createPostWithImages(
                IntStream.range(0, req.getImages().size()).mapToObj(i -> createImage()).collect(toList()))
        );

        // when
        postService.create(req);

        // then
        verify(postRepository).save(any());
        verify(fileService, times(req.getImages().size())).upload(any(), anyString());
    }

    @Test
    void createExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.create(createPostCreateRequest())).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void createExceptionByCategoryNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.create(createPostCreateRequest())).isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void createExceptionByUnsupportedImageFormatExceptionTest() {
        // given
        PostCreateRequest req = createPostCreateRequestWithImages(
                List.of(new MockMultipartFile("test", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes()))
        );
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));

        // when, then
        assertThatThrownBy(() -> postService.create(req)).isInstanceOf(UnsupportedImageFormatException.class);
    }

    @Test
    void readTest() {
        // given
        Post post = createPostWithImages(List.of(createImage(), createImage()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        PostDto postDto = postService.read(1L);

        // then
        assertThat(postDto.getTitle()).isEqualTo(post.getTitle());
        assertThat(postDto.getImages().size()).isEqualTo(post.getImages().size());
    }

    @Test
    void readExceptionByPostNotFoundTest() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.read(1L)).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void deleteTest() {
        // given
        Post post = createPostWithImages(List.of(createImage(), createImage()));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        // when
        postService.delete(1L);

        // then
        verify(fileService, times(post.getImages().size())).delete(anyString());
        verify(postRepository).delete(any());
    }

    @Test
    void deleteExceptionByNotFoundPostTest() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.delete(1L)).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void updateTest() {
        // given
        Image a = createImageWithIdAndOriginName(1L, "a.png");
        Image b = createImageWithIdAndOriginName(2L, "b.png");
        Post post = createPostWithImages(List.of(a, b));
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        MockMultipartFile cFile = new MockMultipartFile("c", "c.png", MediaType.IMAGE_PNG_VALUE, "c".getBytes());
        PostUpdateRequest postUpdateRequest = createPostUpdateRequest("title", "content", 1000L, List.of(cFile), List.of(a.getId()));

        // when
        postService.update(1L, postUpdateRequest);

        // then
        List<Image> images = post.getImages();
        List<String> originNames = images.stream().map(i -> i.getOriginName()).collect(toList());
        assertThat(originNames.size()).isEqualTo(2);
        assertThat(originNames).contains(b.getOriginName(), cFile.getOriginalFilename());

        verify(fileService, times(1)).upload(any(), anyString());
        verify(fileService, times(1)).delete(anyString());
    }

    @Test
    void updateExceptionByPostNotFoundTest() {
        // given
        given(postRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> postService.update(1L, createPostUpdateRequest("title", "content", 1234L, List.of(), List.of())))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void readAllTest() {
        // given
        given(postRepository.findAllByCondition(any())).willReturn(Page.empty());

        // when
        PostListDto postListDto = postService.readAll(createPostReadCondition(1, 1));

        // then
        assertThat(postListDto.getPostList().size()).isZero();
    }
}