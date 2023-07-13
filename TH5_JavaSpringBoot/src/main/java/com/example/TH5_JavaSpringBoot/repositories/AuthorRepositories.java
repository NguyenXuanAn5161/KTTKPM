package com.example.TH5_JavaSpringBoot.repositories;

import com.example.TH5_JavaSpringBoot.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepositories extends JpaRepository<Author,Long> {
    List<Author> findByName(String name);

}