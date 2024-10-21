package com.OlegKulikov.pastbinclone.try_1.Repositories;

import com.OlegKulikov.pastbinclone.try_1.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<UserComment, Integer> {
    List<UserComment> findByText(Text text);
}