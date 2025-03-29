package backend.Repositories;

import backend.model.Rating;
import backend.model.Text;
import backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    boolean existsByTextAndUser(Text text, User user);
    Optional<Rating> findByTextAndUser(Text text, User user);
}
