package com.janghankyu.kokoro.factory.entity;

import com.janghankyu.kokoro.entity.category.Category;
import com.janghankyu.kokoro.entity.member.Member;
import com.janghankyu.kokoro.entity.post.Image;
import com.janghankyu.kokoro.entity.post.Post;

import java.util.List;

import static com.janghankyu.kokoro.factory.entity.CategoryFactory.createCategory;
import static com.janghankyu.kokoro.factory.entity.MemberFactory.createMember;

public class PostFactory {
    public static Post createPost() {
        return createPost(createMember(), createCategory());
    }

    public static Post createPost(Member member, Category category) {
        return new Post("title", "content", 1000L, member, category, List.of());
    }

    public static Post createPostWithImages(Member member, Category category, List<Image> images) {
        return new Post("title", "content", 1000L, member, category, images);
    }

    public static Post createPostWithImages(List<Image> images) {
        return new Post("title", "content", 1000L, createMember(), createCategory(), images);
    }
}
