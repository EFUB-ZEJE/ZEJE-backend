package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.User;
import lombok.Getter;
import org.hibernate.Session;

import java.io.Serializable;

@Getter
public class SessionUserDTO implements Serializable {
    private Long userId;
    private Long kakaoId;
    private String nickname;
    private String email;
    private String profileUrl;

    public SessionUserDTO(User user) {
        this.kakaoId = user.getKakaoId();
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileUrl();
    }
}
