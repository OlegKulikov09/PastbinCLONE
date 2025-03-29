package backend.Repositories;

import backend.model.Text;
import backend.model.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<UserComment, UUID> {
    List<UserComment> findByText(Text text);
}