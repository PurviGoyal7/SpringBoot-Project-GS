package com.example.growsimpleeintern.Services;

import com.example.growsimpleeintern.Model.User;
import com.example.growsimpleeintern.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepo usersRepo;

    public User loadUserById(String Id) throws UsernameNotFoundException {
        try {
            Optional<User> user = usersRepo.findById(Integer.valueOf(Id));
            return user.get();
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("User not found with id: " + Id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
