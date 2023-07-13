package com.example.TH5_JavaSpringBoot.database;

import com.example.TH5_JavaSpringBoot.repositories.AuthorRepositories;
import com.example.TH5_JavaSpringBoot.repositories.CourseRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(AuthorRepositories authorRepositories, CourseRepositories courseRepositories){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {}
        };
    }
}

