package com.pet.petproject.login.user;

import com.pet.petproject.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@AllArgsConstructor
@ToString
@Getter
public class UserPrincipal implements UserDetails, OAuth2User {

    private Member member;
    private Map<String, Object> oauth2UserAttributes;

    public UserPrincipal(Member member){
        this.member = member;
    }

    public static UserPrincipal create(Member member, Map<String, Object> oauth2UserAttributes) {
        return new UserPrincipal(member, oauth2UserAttributes);
    }

    public static UserPrincipal create(Member member){
        return new UserPrincipal(member, new HashMap<>());
    }

    public Member getMember(){
        return this.member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authorityList.add(authority);

        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.member.getPassword();
    }

    @Override
    public String getUsername() {
        // User를 authentication 할 때 사용할 username을 return.
        // 실제 username이 아니라 PK 값인 memberNo를 넘겨준다
        return String.valueOf(this.member.getId());
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠김 여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부
        return true;
    }

    @Override
    @Nullable
    public <A> A getAttribute(String name) {
        return (A) oauth2UserAttributes.get(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(oauth2UserAttributes);
    }

    @Override
    public String getName() {
        return member.getName();
    }
}
