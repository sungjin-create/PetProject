package com.pet.petproject.common.oauth2;

import com.pet.petproject.member.entity.Member;
import com.pet.petproject.member.entity.SocialType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Builder
public class OAuth2Attribute {

    private String socialId;
    private SocialType socialType;

    public static OAuth2Attribute of(SocialType socialType, Map<String, Object> attributes){

        //소셜 타입에 맞게 id값을 가져온다

        if (socialType.equals(SocialType.GOOGLE)) {
            return ofGoogle(socialType, attributes);
        }

        if (socialType.equals(SocialType.NAVER)) {
            return ofNAVER(socialType, attributes);
        }

        return ofKakao(socialType, attributes);
    }

    private static OAuth2Attribute ofKakao(SocialType socialType, Map<String, Object> attributes) {
        return OAuth2Attribute.builder()
                .socialId(String.valueOf(attributes.get("id")))
                .socialType(socialType)
                .build();
    }

    private static OAuth2Attribute ofNAVER(SocialType socialType, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2Attribute.builder()
                .socialType(socialType)
                .socialId((String) response.get("id"))
                .build();
    }

    private static OAuth2Attribute ofGoogle(SocialType socialType, Map<String, Object> attributes){
        return OAuth2Attribute.builder()
                .socialId((String) attributes.get("sub"))
                .socialType(socialType)
                .build();
    }

    public Member toEntity(OAuth2Attribute oAuth2Attribute) {
        return Member.builder()
                .id(UUID.randomUUID().toString())
                .socialId(oAuth2Attribute.socialId)
                .socialType(oAuth2Attribute.socialType)
                .registerDate(LocalDateTime.now())
                .build();
    }

    public Map<String, Object> toMap(OAuth2Attribute oAuth2Attribute) {
        Map<String, Object> map = new HashMap<>();
        map.put("socialId", oAuth2Attribute.socialId);
        map.put("socialType", oAuth2Attribute.socialType);
        return map;
    }
}
