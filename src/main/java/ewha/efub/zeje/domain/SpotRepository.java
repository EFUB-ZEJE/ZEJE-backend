package ewha.efub.zeje.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
    List<Spot> findByCategory(String category);
    List<Spot> findByCategoryAndNameContaining(String category, String name);
    Optional<Spot> findByContentId(Long contentId);
}
