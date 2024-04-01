package com.example.registrationlogindemo.repository;

import com.example.registrationlogindemo.entity.Todo;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // Additional methods if needed

    @Override
    void deleteById(Long aLong);
}
