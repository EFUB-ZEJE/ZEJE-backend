package ewha.efub.zeje.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //탈퇴하지 않은 유저 리스트 반환용
    List<User> findAllByDeleteFlagFalse();

    //특정 유저 반환용
    User findByUserIdAndDeleteFlagFalse(Long userId);

    //로그인인지 회원가입인지 확인용
    User findByKakaoId(Long kakaoId);

    User findByEmail(String email);
}
