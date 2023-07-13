package com.example.TH5_JavaSpringBoot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tbtCourse")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String title;
    private Double cost;
    private Double curPrice;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private Author author;

    public Course() {
    }

    public Course(String name, String title, Double cost, Double curPrice, Author author) {
        this.name = name;
        this.title = title;
        this.cost = cost;
        this.curPrice = curPrice;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(Double curPrice) {
        this.curPrice = curPrice;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", cost=" + cost +
                ", curPrice=" + curPrice +
                ", author=" + author +
                '}';
    }
}
