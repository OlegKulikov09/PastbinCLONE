package com.OlegKulikov.pastbinclone.try_1.Repositories;

import com.OlegKulikov.pastbinclone.try_1.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TextRepository extends JpaRepository<Text, Long> {
    List<Text> findByUserId(Long userId);
}
