package com.torm.movierecommender.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "movies")
@NoArgsConstructor
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long movieId;

    @Getter @Setter
    private String title;

    @Getter @Setter
    private Integer year;

    @Getter @Setter
    private String genres;

    @Column(columnDefinition = "TEXT")
    @Getter @Setter
    private String plot;

    @Column(columnDefinition = "TEXT")
    @Getter @Setter
    private String embeddings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter @Setter
    private UserEntity user;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<RatingEntity> ratings;
}