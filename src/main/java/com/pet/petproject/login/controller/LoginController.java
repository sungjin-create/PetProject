package com.pet.petproject.login.controller;

import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.login.service.LoginService;
import com.pet.petproject.login.model.JoinDto;
import com.pet.petproject.login.model.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    //이메일로 회원가입
    @PostMapping("/join/mail/member")
    public ResponseEntity<?> join(@Valid @RequestBody JoinDto parameter) {
        loginService.join(parameter);
        return ResponseEntity.ok().body("회원 가입 완료, 이메일 인증이 필요합니다.");
    }

    //이메일 인증
    @GetMapping("/join/mail/auth")
    public ResponseEntity<?> emailAuth(HttpServletRequest request) {
        String id = request.getParameter("id");
        log.info("id = {}", id);
        return ResponseEntity.ok().body("인증 결과 : " + loginService.emailAuthGranted(id));
    }

    //이메일로 로그인
    @PostMapping("/login/mail/member")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto parameter) {
        return ResponseEntity.ok().body(loginService.login(parameter));
    }

    //oauth2 토큰 발급 페이지
    @GetMapping("/oauth/token/provide/{token}")
    public ResponseEntity<?> oauthTokenProvide(@PathVariable(name = "token") String token) {
        return ResponseEntity.ok().body("Bearer " + token);
    }

    //인가 테스트 페이지
    @GetMapping("/test/auth")
    public String testAuth() {
        return "인가 성공";
    }
}