package com.janghankyu.kokoro.controller.member;

import com.janghankyu.kokoro.dto.response.Response;
import com.janghankyu.kokoro.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/api/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response read(@PathVariable Long id) {
        return Response.success(memberService.read(id));
    }

    @DeleteMapping("/api/members/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@PathVariable Long id) {
        memberService.delete(id);
        return Response.success();
    }
}
