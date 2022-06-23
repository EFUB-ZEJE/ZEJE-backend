package ewha.efub.zeje.user;

import ewha.efub.zeje.domain.User;
import ewha.efub.zeje.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserDomainTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 생성 테스트")
    void createUser() {
        User user1 = User.builder()
                .nickname("유저1")
                .profileUrl("kakao-default.jpg")
                .email("efub@gmail.com")
                .build();

        userRepository.save(user1);
        User user2 = userRepository.findByEmail("efub@gmail.com");

        System.out.println("user1 = " + user1.getUserId() + ", " + user1.getEmail());
        System.out.println("user2 = " + user2.getUserId() + ", " + user2.getEmail());

        assertThat(user1).isSameAs(user2);
    }
}
