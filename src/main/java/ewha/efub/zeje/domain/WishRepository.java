package ewha.efub.zeje.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByUser_UserId(Long userId);
    Optional<Wish> findByUser_UserIdAndSpot_SpotId(Long userId, Long spotId);
    Boolean existsByUser_UserIdAndSpot_SpotId(Long userId, Long spotId);
}
