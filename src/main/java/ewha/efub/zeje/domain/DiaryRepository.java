package ewha.efub.zeje.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUser_UserId(Long userId);
    Optional<Diary> findByUser_userIdAndDiaryId(Long userId, Long diaryId);
    Boolean existsByUser_UserIdAndDiaryId(Long userId, Long diaryId);
    Optional<Diary> findByDiaryId(Long diaryId);
}
