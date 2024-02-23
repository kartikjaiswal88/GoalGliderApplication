package com.example.registrationlogindemo.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;

@Entity(name="Todo")
public class Todo {
    @Id
    @GeneratedValue
    private int id;
    private String description;
    private LocalDate targetDate;
    private boolean done;

    @ManyToOne // Many Todos can belong to One User
    private User user;

    public Todo() {}

    public Todo(String description, LocalDate targetDate, boolean done, User user) {
        this.description = description;
        this.targetDate = targetDate;
        this.done = done;
        this.user = user;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



}