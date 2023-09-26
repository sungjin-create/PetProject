package com.pet.petproject.common.config;

import com.pet.petproject.common.jwt.JwtAuthTokenProvider;
import com.pet.petproject.common.jwt.filter.JwtFilter;
import com.pet.petproject.common.oauth2.handler.OAuth2FailureHandler;
import com.pet.petproject.common.oauth2.handler.OAuth2SuccessHandler;
import com.pet.petproject.common.oauth2.service.OAuth2MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

  private final JwtAuthTokenProvider jwtAuthTokenProvider;
  private final OAuth2SuccessHandler oAuth2SuccessHandler;
  private final OAuth2FailureHandler oAuth2FailureHandler;
  private final OAuth2MemberService oAuth2MemberService;

  @Bean
  protected SecurityFilterChain webSecurityFilterChain(HttpSecurity http) throws Exception {
    http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/login/**", "/join").permitAll()
            .antMatchers("/test/**").authenticated()

            .and()
            .oauth2Login().loginPage("/")
            .successHandler(oAuth2SuccessHandler)
            .failureHandler(oAuth2FailureHandler)
            .userInfoEndpoint().userService(oAuth2MemberService);
        http.addFilterBefore(new JwtFilter(jwtAuthTokenProvider), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
