package ewha.efub.zeje.dto.user;

import ewha.efub.zeje.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoProfile {
    private Long id;
    private String nickname;
    private String email;
    private String profileUrl;

    public User toEntity() {
        User user = User.builder()
                        .kakaoId(this.id)
                        .profileUrl(this.profileUrl)
                        .nickname(this.nickname)
                        .email(this.email)
                        .build();
        return user;
    }
}
