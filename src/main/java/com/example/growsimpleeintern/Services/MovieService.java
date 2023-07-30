package com.example.growsimpleeintern.Services;

import com.example.growsimpleeintern.Model.Movie;
import com.example.growsimpleeintern.Repository.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Value("${api.key}")  //api.key being fetched from application.properties file
    private String apiKey;

    @Autowired
    private MovieRepo movieRepo;
    public String addMovie()
    {
            boolean flag = false;
            String response = "";
            try{
                movieRepo.deleteAll();
                for(int i = 60; i<=120; i++) {   //fetching only few movies as the api returns large amount of data
                    String url = "https://api.themoviedb.org/3/movie/" + i + "?api_key=" + apiKey;
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    HttpEntity<String> entity = new HttpEntity<String>(headers);
                    try{
                        ResponseEntity<Movie> output = restTemplate.exchange(url, HttpMethod.GET, entity, Movie.class);
                        movieRepo.save(output.getBody());
                        flag = true;
                    } catch(Exception e){
                        System.out.println("Error adding a movie in database1");
                    }
                }
            } catch(Exception e) {
                System.out.println("Error adding a movie in database2");
                response =  "Error fetching movies";
            }
            if(flag)
                response = "Movie Added";
            return response;
    }

    public long rateMovie(int id, long rating)
    {
        long currAvgRating = 0;
        long newAvgRating = 0;
        Optional<Movie> mov = movieRepo.findById(id);

        if(mov.isPresent())
        {
            Movie movie = mov.get();
            currAvgRating = movie.getAvgRating();
            if(currAvgRating == 0)
                newAvgRating = currAvgRating;
            else
                newAvgRating = (currAvgRating + rating)/2;
            movie.setAvgRating(newAvgRating);
            movieRepo.save(movie);
        }
        return newAvgRating;
    }

    public String movieAvgRating(int id) {
        String avgRating = "NA";
        Optional<Movie> mov = movieRepo.findById(id);
        if(mov.isPresent()) {
            Movie movie = mov.get();
            if(movie.getAvgRating()!=0)
                avgRating = String.valueOf(movie.getAvgRating());
        }
        return avgRating;
    }

    public List<Movie> movieList() {
        List<Movie> movies = movieRepo.findAll();
        return movies;
    }
}
