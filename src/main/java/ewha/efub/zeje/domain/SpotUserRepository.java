package ewha.efub.zeje.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotUserRepository extends JpaRepository<SpotUser, Long> {
    public Boolean existsBySpotAndUser(Spot spot, User user);
}
