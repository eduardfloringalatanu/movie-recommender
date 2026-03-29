package com.torm.movierecommender.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "title", "directors", "user_id" })
})
@NoArgsConstructor
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long movieId;

    @Getter @Setter
    private String title;

    @Getter @Setter
    private Short releaseYear;

    @Getter @Setter
    private String directors;

    @Getter @Setter
    private String genres;

    @Column(columnDefinition = "TEXT")
    @Getter @Setter
    private String plot;

    @Column(columnDefinition = "TEXT")
    @Getter @Setter
    private String embeddings;

    @Getter @Setter
    private Short rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter @Setter
    private UserEntity user;
}