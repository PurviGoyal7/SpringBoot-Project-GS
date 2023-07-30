package com.example.growsimpleeintern.Repository;

import com.example.growsimpleeintern.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepo extends JpaRepository<Movie, Integer> {

}
