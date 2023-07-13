package com.example.TH5_JavaSpringBoot.controller;

import com.example.TH5_JavaSpringBoot.entity.Course;
import com.example.TH5_JavaSpringBoot.repositories.CourseRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/Course")
public class Coursecontroller {
    @Autowired
    private CourseRepositories courseRepositories;
    @GetMapping("")
    List<Course> getAllCourse(){
        return courseRepositories.findAll();
    }
    //TODO: Tìm theo id
    @GetMapping("/{id}")
    Course findById(@PathVariable Long id){
        return courseRepositories.findById(id).orElseThrow(()->new RuntimeException("Không thể tìm thấy"));
    }
}
