package com.example.growsimpleeintern.Repository;

import com.example.growsimpleeintern.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmailId(String emailId);
}
