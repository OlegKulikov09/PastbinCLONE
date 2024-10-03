package com.OlegKulikov.pastbinclone.try_1.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Random;

@Data
@Entity
@Table (name = "texts")
public class Text {
    @Id
    private int textId;
    @PrePersist
    public void generateId() {
        if (this.textId == 0) {
            Random random = new Random();
            this.textId = 100000 + random.nextInt(900000);
        }
    }

    private String title;
    @Column(length = 3000)
    private String content;
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
