package com.torm.movierecommender.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movies", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "title", "release_year", "directors", "user_id" })
})
@NoArgsConstructor
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long movieId;

    @Column(nullable = false, length = 500)
    @Getter @Setter
    private String title;

    @Column(nullable = false)
    @Getter @Setter
    private Short releaseYear;

    @Column(nullable = false, length = 1500)
    @Getter @Setter
    private String directors;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Getter @Setter
    private String genres;

    @Column(nullable = false, length = 1000)
    @Getter @Setter
    private String plot;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Getter @Setter
    private String embeddings;

    @Getter @Setter
    private Short rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter @Setter
    private UserEntity user;
}