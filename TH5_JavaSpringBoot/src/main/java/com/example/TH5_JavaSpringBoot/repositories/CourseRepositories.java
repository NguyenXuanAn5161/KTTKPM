package com.example.TH5_JavaSpringBoot.repositories;

import com.example.TH5_JavaSpringBoot.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepositories extends JpaRepository<Course,Long> {
    List<Course> findByName(String name);
}
