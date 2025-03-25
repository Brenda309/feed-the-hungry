package com.brenda.FeedTheHungry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.brenda.FeedTheHungry.model.user.User;
import java.util.*;



public interface UserRepository extends JpaRepository<User, Integer>{
Optional <User> findByEmail(String email);
}
