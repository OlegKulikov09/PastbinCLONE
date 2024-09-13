package com.OlegKulikov.pastbinclone.try_1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Data
@Entity
@Table(name = "userRoles")
public class Role implements GrantedAuthority {
    @Id
    private int id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {}

    @Override
    public String getAuthority() {
        return getName();
    }
}
