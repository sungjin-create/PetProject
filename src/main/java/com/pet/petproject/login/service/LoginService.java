package com.pet.petproject.login.service;

import com.pet.petproject.common.exception.AppException;
import com.pet.petproject.common.jwt.JwtAuthTokenProvider;
import com.pet.petproject.login.components.MailComponents;
import com.pet.petproject.login.model.JoinDto;
import com.pet.petproject.login.model.LoginDto;
import com.pet.petproject.login.user.UserPrincipal;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    private final JwtAuthTokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final MailComponents mailComponents;


    public void join(JoinDto parameter) {
        String name = parameter.getName();
        String email = parameter.getEmail();
        String password = parameter.getPassword();

        //이미 가입한 사람인지 체크
        if (memberRepository.existsByEmail(email)) {
            throw new AppException(HttpStatus.BAD_REQUEST, email + "는 이미 있습니다.");
        }

        String uuid = UUID.randomUUID().toString();
        Member member = Member.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .email(email)
                .password(encoder.encode(password))
                .emailAuthYn(false)
                .emailAuthKey(uuid)
                .registerDate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        sendEmailAuth(email, uuid);

    }

    //이메일로 로그인
    public String login(LoginDto parameter) {

        String email = parameter.getEmail();
        String password = parameter.getPassword();

        Member member = memberRepository.findByEmail(email).orElseThrow(
                ()->new AppException(HttpStatus.BAD_REQUEST, "이메일 오류 입니다"));

        //이메일 인증여부 검사
        if (!member.isEmailAuthYn()) {
            throw new AppException(HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다.");
        }

        // 비밀번호 일치여부 검사
        if(!encoder.matches(password, member.getPassword())){
            throw new AppException(HttpStatus.BAD_REQUEST, "비밀번호 오류입니다.");
        }

        // 인증 성공
        // member 객체를 포함한 userPrincipal 생성
        UserPrincipal userPrincipal = UserPrincipal.create(member);

        // Authentication에 담을 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, Collections.singleton(new SimpleGrantedAuthority("USER")));

        // security context에 저장
        SecurityContextHolder.getContext().setAuthentication(token);

        // 인코딩된 값 리턴
        return tokenProvider.createAuthToken(userPrincipal).getToken();
    }


    //인증을 위한 이메일 보내기
    private void sendEmailAuth(String email, String uuid) {
        String subject = "PetProject에 가입을 축하드립니다.";
        String text = "<p>PetProject에 가입을 축하드립니다.</p><p>아래 링크를 클릭하셔서 가입을 완료 하세요.</p>" +
                "<div><a href='http://localhost:8080/join/mail/auth?id=" + uuid + "'>가입완료</a></div>";
        mailComponents.sendMail(email, subject, text);
    }


    //이메일 인증 권한 부여하기
    public boolean emailAuthGranted(String uuid) {

        //이메일 인증키로 체크
        Member member = memberRepository.findByEmailAuthKey(uuid).orElseThrow(()->new RuntimeException("이메일 인증 값이 유효하지 않습니다"));
        member.setEmailAuthYn(true);
        member.setEmailAuthDate(LocalDateTime.now());
        memberRepository.save(member);
        return true;
    }

}
