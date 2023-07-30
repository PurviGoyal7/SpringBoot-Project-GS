package com.example.growsimpleeintern.Controller;

import com.example.growsimpleeintern.Model.User;
import com.example.growsimpleeintern.Model.UserMovieRating;
import com.example.growsimpleeintern.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check")
    public String check() {
        return "OK";
    }

    @PostMapping("/registration")
    public String userRegistration(@RequestBody User user) {
        String res = userService.addUser(user);
        return res;
    }

    @PostMapping("/login")
    public String userLogin(@RequestBody User user) {
        String res = userService.loginUser(user);
        return res;
    }

    @PostMapping("/rateMovie")
    public String userMovieRating(@RequestBody UserMovieRating userMovieRating, @RequestHeader("Authorization") String jwtToken) {
        jwtToken = jwtToken.substring(7);
        return userService.userMovieRating(userMovieRating,jwtToken);
    }

}
