package ewha.efub.zeje.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {
    List<Memory> findMemoriesByDiary_DiaryId(Long diaryId);
    Boolean existsMemoryByDiary_DiaryIdAndMemoryId(Long diaryId, Long memoryId);
    Optional<Memory> findMemoryByMemoryId(Long memoryId);

}