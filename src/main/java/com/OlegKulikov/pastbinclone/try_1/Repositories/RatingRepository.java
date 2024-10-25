package com.OlegKulikov.pastbinclone.try_1.Repositories;

import com.OlegKulikov.pastbinclone.try_1.model.Rating;
import com.OlegKulikov.pastbinclone.try_1.model.Text;
import com.OlegKulikov.pastbinclone.try_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
    boolean existsByTextAndUser(Text text, User user);
    Optional<Rating> findByTextAndUser(Text text, User user);
}
