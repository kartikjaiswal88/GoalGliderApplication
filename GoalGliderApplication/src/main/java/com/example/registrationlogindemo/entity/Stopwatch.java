package com.example.registrationlogindemo.entity;

import java.time.Duration;
import java.time.Instant;

public class Stopwatch {
    private Instant startTime;
    private boolean running;
    private long elapsedTimeMillis;

    public void start() {
        if (!running) {
            startTime = Instant.now();
            running = true;
        }
    }

    public void stop() {
        if (running) {
            elapsedTimeMillis += Duration.between(startTime, Instant.now()).toMillis();
            running = false;
        }
    }

    public long getElapsedTimeMillis() {
        if (running) {
            return elapsedTimeMillis + Duration.between(startTime, Instant.now()).toMillis();
        } else {
            return elapsedTimeMillis;
        }
    }
}
