package com.OlegKulikov.pastbinclone.try_1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Random;

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
    private String content;
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public int getTextId() {
        return textId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
