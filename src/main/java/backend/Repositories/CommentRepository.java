package backend.Repositories;

import backend.model.Text;
import backend.model.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<UserComment, Integer> {
    List<UserComment> findByText(Text text);
}