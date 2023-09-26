package com.pet.petproject.login.service;

import com.pet.petproject.login.user.UserPrincipal;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    /**
     * @param username memberNo가 저장되어 있다
     * @return member 객체를 포함한 UserPrincipal -> UsernamePasswordAuthenticationToken에 넣는다
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다."));

        return new UserPrincipal(member);
    }
}
