package backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
    private int rate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "text", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rating> ratings = new HashSet<>();
}
