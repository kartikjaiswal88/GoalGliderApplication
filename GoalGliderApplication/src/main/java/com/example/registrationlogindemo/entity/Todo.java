package com.example.registrationlogindemo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.time.Instant;
import java.time.LocalDate;
import java.time.Duration;

@Entity(name = "Todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 8, max = 30)
    private String description;

    @NotNull
    @FutureOrPresent
    private LocalDate targetDate;

    private boolean done;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private long totalTime;
    private Instant stopwatchStartTime;

    public Todo() {
    }

    public Todo(String description, LocalDate targetDate, boolean done, User user) {
        this.description = description;
        this.targetDate = targetDate;
        this.done = done;
        this.user = user;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public Instant getStopwatchStartTime() {
        return stopwatchStartTime;
    }

    public void setStopwatchStartTime(Instant stopwatchStartTime) {
        this.stopwatchStartTime = stopwatchStartTime;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", targetDate=" + targetDate +
                ", done=" + done +
                ", user=" + user +
                '}';
    }
}
