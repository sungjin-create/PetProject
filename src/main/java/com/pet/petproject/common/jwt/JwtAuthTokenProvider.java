package com.pet.petproject.common.jwt;

import co.elastic.clients.util.DateTime;
import com.pet.petproject.login.user.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthTokenProvider {

    // property의 값을 읽어오는 어노테이션
    @Value("${jwt.secret}")
    private String secret;
    private Key key;

    private final UserDetailsService userDetailsService;

    @PostConstruct // 의존성 주입 후 초기화
    public void init(){
        // base64를 byte[]로 변환
        byte[] keyBytes = Base64Utils.decodeFromUrlSafeString(secret);
        // byte[]로 Key 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 일반/소셜 로그인 성공 시 UserPrincipal을 만들어 전달하면 JwtAuthToken 발급
     * LoginService, OAuth2SuccessHandler에서 사용
     * @param userPrincipal Authenticaion에 넣어 Security Context에 저장할 유저 정보
     * @return JwtAuthToken 객체
     */

    public JwtAuthToken createAuthToken(UserPrincipal userPrincipal){
        // PK
        String id = userPrincipal.getMember().getId();

        // claims 만들기
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", id);
        claims.put("socialId", userPrincipal.getMember().getSocialId());

        // 기한
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(180).atZone(ZoneId.systemDefault()).toInstant());

        return new JwtAuthToken(id, key, claims, expiredDate);
    }

    /**
     * JwtFilter에서 사용
     * 헤더에서 받아온 token을 주면 이 클래스의 멤버변수로 지정된 key 값을 포함하여 JwtAuthToken 객체 리턴
     * 유효한 토큰인지 확인하기 위해 쓴다.
     * @param token 헤더에서 받아온 token
     * @return JwtAuthToken 객체
     */

    public JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token, key);
    }

    /**
     * JwtFilter에서 유효한 토큰인지를 확인한 후 Security Context에 저장할 Authentication 리턴
     * @param authToken 헤더에 담겨 온 Jwt를 decode한 것
     * @return UsernamePasswordAuthenticationToken(userPrincipal, null, role)
     */

    public Authentication getAuthentication(JwtAuthToken authToken) {
        if(authToken.validate()){
            // authToken에 담긴 데이터를 받아온다
            Claims claims = authToken.getData();

            UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(claims.getSubject());

            return new UsernamePasswordAuthenticationToken(
                    userPrincipal,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("USER")));
        } else {
            throw new JwtException("token error");
        }
    }
}