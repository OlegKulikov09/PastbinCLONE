package com.OlegKulikov.pastbinclone.try_1.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    private String content;
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
