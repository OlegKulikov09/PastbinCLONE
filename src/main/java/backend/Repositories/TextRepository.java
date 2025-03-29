package backend.Repositories;

import backend.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TextRepository extends JpaRepository<Text, String> {
    List<Text> findByUserId(UUID id);
    List<Text> findByTextId(String textId);
    @Query(value = "SELECT * FROM texts ORDER BY rate DESC LIMIT 10", nativeQuery = true)
    List<Text> orderByRate10Desc();
}
