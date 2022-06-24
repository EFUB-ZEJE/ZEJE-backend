package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.domain.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@Getter
public class OAuthAttributesDTO {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributesDTO(Map<String, Object> attributes, String nameAttributeKey, String name,
                           String email, String picture){
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
                .name((String)profile.get("nickname"))
                .email((String)response.get("email"))
                .picture((String)profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    public User toEntity(String registrationId){//처음 가입할 때 User entity 생성
        User newUser = User.builder()
                .nickname(name)
                .profileUrl(picture)
                .email(email)
                .build();
        return newUser;
    }
}
