package com.OlegKulikov.pastbinclone.try_1.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Size;
import java.util.*;

@Data
@Entity
@Table(name = "userList")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String surname;
    @Column
    @Size(min = 3, message = "No less 3 letters")
    private String login;
    @Column
    @Size(min = 3, message = "No less 3 letters")
    private String password;
    @Column
    private String email;
    @Column
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Text> texts = new ArrayList<>();
}
