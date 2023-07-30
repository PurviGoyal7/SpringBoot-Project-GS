package com.example.growsimpleeintern.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserMovieRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "movie_id")
    private int movieId;

    @Column(name = "rating")
    private int movieRating;

    public UserMovieRating(int userId, int movieId, int movieRating) {
        this.userId = userId;
        this.movieId = movieId;
        this.movieRating = movieRating;
    }
}
