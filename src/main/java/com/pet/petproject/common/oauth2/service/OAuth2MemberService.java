package com.pet.petproject.common.oauth2.service;

import com.pet.petproject.common.oauth2.OAuth2Attribute;
import com.pet.petproject.login.user.UserPrincipal;
import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.entity.SocialType;
import com.pet.petproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2MemberService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //인증이 성공한 뒤 DefaultOAuth2UserService 객체를 만든 뒤 User를 받는다
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        //registrationId를 가져온다. registrationId에는 구글, 네이버, 카카오 정보가 들어있다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //socialType으로 변환
        SocialType socialType = SocialType.valueOf(registrationId.toUpperCase());

        //oAuth2Attribute에 socialId값과 socialType을 저장한다
        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(socialType, oAuth2User.getAttributes());

        //신규회원이면 save, 기존회원이면 회원정보를 가져온다.
        Member member = saveOrUpdate(oAuth2Attribute);

        //attributes를 Member 테이블의 PK와 함께 Security Context에 저장하기 위해 map으로 변환한다.
        Map<String, Object> attributes = oAuth2Attribute.toMap(oAuth2Attribute);

        return UserPrincipal.create(member, attributes);
    }

    public Member saveOrUpdate(OAuth2Attribute oAuth2Attribute){
        //기존에 회원이 없으면 oAuth2Attribute의 정보를 기반으로 Member 객체 만들어서 저장한다.
        Member member = memberRepository.findBySocialIdAndSocialType(oAuth2Attribute.getSocialId(), oAuth2Attribute.getSocialType())
                .orElse(oAuth2Attribute.toEntity(oAuth2Attribute));

        return memberRepository.save(member);
    }
}
