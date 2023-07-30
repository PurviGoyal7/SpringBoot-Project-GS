package com.example.growsimpleeintern.Services;

import com.example.growsimpleeintern.Model.Movie;
import com.example.growsimpleeintern.Model.User;
import com.example.growsimpleeintern.Model.UserMovieRating;
import com.example.growsimpleeintern.Repository.MovieRepo;
import com.example.growsimpleeintern.Repository.UserMovieRatingRepo;
import com.example.growsimpleeintern.Repository.UserRepo;
import com.example.growsimpleeintern.Security.JwtTokenHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private MovieRepo movieRepo;

    @Autowired
    private UserMovieRatingRepo userMovieRatingRepo;

    public String addUser(User user) {
        String response = "";
        if(userRepo.findByEmailId(user.getEmailId()) != null)
            response = "User already exists";
        else {
            try{
                authenticate(user);
                userRepo.save(user);
                response = "User added Successfully";
            } catch(Exception e) {
                response = "Error while registration of this account";
            }
        }
        return response;
    }

    public void authenticate(User user) throws Exception {
        String pass = user.getPassword();
        if(pass.length() < 3) {
            throw new Exception("Please enter password of length greater than 3");
        }
    }

    public String loginUser(User user) {
        String token = "";
        String mail = user.getEmailId();
        String pass = user.getPassword();
        if(userRepo.findByEmailId(mail) != null) {
            User temp = userRepo.findByEmailId(mail);
            System.out.println(temp);
            if(temp.getEmailId().equals(mail) && temp.getPassword().equals(pass)) {
                token = jwtTokenHelper.generateToken(temp);
                token = "Bearer "+token;
                System.out.println("Token: " + token);
            } else{
                System.out.println("email or pwd error");
            }
        }
        if(token.equalsIgnoreCase(""))
            token = "Invalid Credentials";
        return token;
    }

    public String userMovieRating(UserMovieRating userMovieRating, String jwtToken) {
        try {
            String Id = jwtTokenHelper.getUserIdFromToken(jwtToken);
            User user = userRepo.findById(Integer.valueOf(Id)).get();
            int userId = (int)user.getId();
            long rating = userMovieRating.getMovieRating();
            int movieId = userMovieRating.getMovieId();

            Optional<Movie> movie = movieRepo.findById(movieId);
            if(!movie.isPresent())
                return "Invalid Movie Id";

            Movie presentMovie = movie.get();
            long newRating = 0;

            if(presentMovie.getAvgRating() == 0)
                newRating = rating;
            else
                newRating = (rating+presentMovie.getAvgRating())/2;

            presentMovie.setAvgRating(newRating);
            movieRepo.save(presentMovie);
            userMovieRating.setUserId(userId);
            userMovieRatingRepo.save(userMovieRating);
            return "Rating added successfully";
        } catch (Exception e) {
            return "Error Adding Custom Rating";
        }
    }
}
