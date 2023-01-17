package com.janghankyu.kokoro.init;

import com.janghankyu.kokoro.entity.category.Category;
import com.janghankyu.kokoro.entity.member.Member;
import com.janghankyu.kokoro.entity.member.MemberRole;
import com.janghankyu.kokoro.exception.MemberNotFoundException;
import com.janghankyu.kokoro.repository.category.CategoryRepository;
import com.janghankyu.kokoro.repository.member.MemberRepository;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class TestInitDB {
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder passwordEncoder;

    @Autowired
    CategoryRepository categoryRepository;

    private String adminEmail = "admin@admin.com";
    private String member1Email = "member1@member.com";
    private String member2Email = "member2@member.com";
    private String password = "123456a!";

    @Transactional
    public void initDB() {
        initTestAdmin();
        initTestMember();
        initCategory();
    }

    private void initTestAdmin() {
        memberRepository.save(
                new Member(adminEmail, passwordEncoder.encode(password), "admin", "admin",
                        MemberRole.ROLE_ADMIN)
        );
    }

    private void initTestMember() {
        memberRepository.saveAll(
                List.of(
                        new Member(member1Email, passwordEncoder.encode(password), "member1", "member1",
                                MemberRole.ROLE_NORMAL),
                        new Member(member2Email, passwordEncoder.encode(password), "member2", "member2",
                                MemberRole.ROLE_NORMAL)
        ));
    }

    private void initCategory() {
        Category category1 = new Category("category1", null);
        Category category2 = new Category("category2", category1);
        categoryRepository.saveAll(List.of(category1, category2));
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getMember1Email() {
        return member1Email;
    }

    public String getMember2Email() {
        return member2Email;
    }

    public String getPassword() {
        return password;
    }
}
