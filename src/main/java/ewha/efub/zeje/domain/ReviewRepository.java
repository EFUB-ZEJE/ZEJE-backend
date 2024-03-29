package ewha.efub.zeje.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    public List<Review> findAllBySpot(Spot spot);
    public List<Review> findAllByUser(User user);
}
