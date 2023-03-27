package com.example.demo;

import com.example.demo.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository for handling request from backend to db
@Repository
public interface GameRepo extends JpaRepository<Game,String> {
}
