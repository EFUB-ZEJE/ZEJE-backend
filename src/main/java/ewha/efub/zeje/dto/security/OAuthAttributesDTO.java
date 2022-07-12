package ewha.efub.zeje.dto.security;

import ewha.efub.zeje.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributesDTO {
    private Map<String, Object> attributes;
    private Long kakaoId;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributesDTO(Map<String, Object> attributes, Long kakaoId, String nameAttributeKey, String name,
                           String email, String picture){
        this.kakaoId = kakaoId;
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributesDTO of(String registrationId, String userNameAttributeName,
                                     Map<String,Object> attributes){
        return ofKakao("id", attributes);
    }

    private static OAuthAttributesDTO ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String,Object> response = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");
        return OAuthAttributesDTO.builder()
                .kakaoId((Long)attributes.get("id"))
                .name((String)profile.get("nickname"))
                .email((String)response.get("email"))
                .picture((String)profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(String registrationId){//처음 가입할 때 User entity 생성
        User newUser = User.builder()
                .kakaoId(kakaoId)
                .nickname(name)
                .profileUrl(picture)
                .email(email)
                .build();
        return newUser;
    }


    public static OAuthAttributesDTO ofPostman(Map<String, Object> attributes) {
        return ofKakaoPostman(attributes);
    }

    private static OAuthAttributesDTO ofKakaoPostman(Map<String, Object> attributes) {
        Map<String,Object> response = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");
        return OAuthAttributesDTO.builder()
                .kakaoId((Long)attributes.get("id"))
                .name((String)profile.get("nickname"))
                .email((String)response.get("email"))
                .picture((String)profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey("id")
                .build();
    }

    public User toEntityPostman(){//처음 가입할 때 User entity 생성
        User newUser = User.builder()
                .kakaoId(kakaoId)
                .nickname(name)
                .profileUrl(picture)
                .email(email)
                .build();
        return newUser;
    }
}
