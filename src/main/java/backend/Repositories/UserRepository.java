package backend.Repositories;

import backend.model.User;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
        Optional <User> findById(UUID id);
        User findByLogin(String login);
        @Query(value =
                "SELECT u.id, u.login, SUM(t.rate) AS sumRateOfUser " +
                "FROM user_list u " +
                "JOIN texts t ON u.id = t.user_id " +
                "GROUP BY u.login, u.id " +
                "ORDER BY sumRateOfUser DESC, u.login ASC;", nativeQuery = true)
        List<Tuple> allUsersOrderByRate();
    }