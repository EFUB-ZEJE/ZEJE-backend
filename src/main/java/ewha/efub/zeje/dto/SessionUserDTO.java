package ewha.efub.zeje.dto;

import ewha.efub.zeje.domain.User;
import lombok.Getter;
import org.hibernate.Session;

import java.io.Serializable;

@Getter
public class SessionUserDTO implements Serializable {
    private String nickname;
    private String email;
    private String profileUrl;

    public SessionUserDTO(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileUrl = user.getProfileUrl();
    }
}
