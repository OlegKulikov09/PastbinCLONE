package com.OlegKulikov.pastbinclone.try_1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table (name = "texts")
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int textId;

    private String title;
    private String content;
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
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
}
