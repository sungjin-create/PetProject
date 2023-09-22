package com.pet.petproject.member.controller;

import com.pet.petproject.member.config.JwtTokenProvider;
import com.pet.petproject.member.model.JoinMember;
import com.pet.petproject.member.model.LoginMember;
import com.pet.petproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    //이메일로 회원가입
    @PostMapping("/mail/join")
    public ResponseEntity<?> join(@RequestBody JoinMember parameter) {
        memberService.join(parameter);
        return ResponseEntity.ok().body("회원 가입 완료, 이메일 인증이 필요합니다.");
    }

    //이메일 인증
    @GetMapping("/mail/auth")
    public ResponseEntity<?> emailAuth(HttpServletRequest request) {
        String id = request.getParameter("id");
        log.info("id = {}", id);
        boolean result = memberService.emailAuthGranted(id);
        return ResponseEntity.ok().body("인증 결과 : " + result);
    }

    //이메일로 로그인
    @PostMapping("/mail/login")
    public ResponseEntity<String> login(@RequestBody LoginMember parameter) {

        String email = parameter.getEmail();
        //회원가입 유무 체크
        boolean joinCheck = memberService.findEmail(email);
        if (!joinCheck) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입이 안되어있습니다.");
        }
        //이메일 인증 체크
        boolean authCheck = memberService.checkEmailAuth(email);
        if (!authCheck) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증이 필요합니다.");
        }

        //비밀번호 일치 체크
        boolean checkPassword = memberService.checkPassword(email, parameter.getPassword());
        if (!checkPassword) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
        }
        //토큰 발급
        return ResponseEntity.ok().body(jwtTokenProvider.createToken(email));
    }

}
