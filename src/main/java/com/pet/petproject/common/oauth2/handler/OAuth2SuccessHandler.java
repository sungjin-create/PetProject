package com.pet.petproject.common.oauth2.handler;

import com.pet.petproject.common.jwt.JwtAuthToken;
import com.pet.petproject.common.jwt.JwtAuthTokenProvider;
import com.pet.petproject.login.user.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    /**
     * 로그인 성공 시 부가작업
     * JWT 발급 후 token과 함께 리다이렉트
     */

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        JwtAuthToken token = jwtAuthTokenProvider.createAuthToken(userPrincipal);

        String targetUrl = "http://localhost:8080/oauth/token/provide/" + token.getToken();

        response.sendRedirect(targetUrl);
    }
}
