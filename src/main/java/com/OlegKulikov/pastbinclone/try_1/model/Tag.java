package com.OlegKulikov.pastbinclone.try_1.model;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String nameOfTag;
    @ManyToMany(mappedBy = "tags")
    private List<Text> texts = new ArrayList<>(); //??????

    public String getNameOfTag() {
        return nameOfTag;
    }

    public void setNameOfTag(String nameOfTag) {
        this.nameOfTag = nameOfTag;
    }

    public int getId() {
        return id;
    }

    public List<Text> getTexts() {
        return texts;
    }
}
