package com.example.growsimpleeintern.Controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.growsimpleeintern.Model.Movie;
import com.example.growsimpleeintern.Services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController  {

    @Autowired
    private MovieService movieService;

    @GetMapping("/addMovie")
    public String addMovie() {
        String res = movieService.addMovie();
        return res;
    }

    @GetMapping("/movieAvgRating/{id}")
    public String movieAvgRating(@PathVariable("id") int id) {
        String res = movieService.movieAvgRating(id);
        return res;
    }

    @GetMapping("/movieList")
    public List<Movie> movieList() {
        List<Movie> movieList = movieService.movieList();
        return movieList;
    }
}
