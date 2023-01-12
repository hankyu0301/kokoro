package com.janghankyu.kokoro.controller.member;

import com.janghankyu.kokoro.dto.sign.SignInResponse;
import com.janghankyu.kokoro.entity.member.Member;
import com.janghankyu.kokoro.exception.MemberNotFoundException;
import com.janghankyu.kokoro.init.TestInitDB;
import com.janghankyu.kokoro.repository.member.MemberRepository;
import com.janghankyu.kokoro.service.member.MemberService;
import com.janghankyu.kokoro.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.janghankyu.kokoro.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
class MemberControllerIntegrationTest {
    @Autowired WebApplicationContext context;
    MockMvc mockMvc;
    @Autowired TestInitDB initDB;
    @Autowired SignService signService;
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();
    }

    @Test
    void readTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                get("/api/members/{id}", member.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse signInRes = signService.signIn(createSignInRequest(initDB.getMember1Email(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId()).header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByAdminTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse adminSignInRes = signService.signIn(createSignInRequest(initDB.getAdminEmail(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId()).header("Authorization", adminSignInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteAccessDeniedByNotResourceOwnerTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse attackerSignInRes = signService.signIn(createSignInRequest(initDB.getMember2Email(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId()).header("Authorization", attackerSignInRes.getAccessToken()))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteUnauthorizedByRefreshTokenTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse signInRes = signService.signIn(createSignInRequest(initDB.getMember1Email(), initDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId()).header("Authorization", signInRes.getRefreshToken()))
                .andExpect(status().isUnauthorized());
    }

}