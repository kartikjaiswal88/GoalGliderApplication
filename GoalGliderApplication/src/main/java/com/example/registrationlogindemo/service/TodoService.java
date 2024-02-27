package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.entity.Todo;
import com.example.registrationlogindemo.entity.User;

import java.util.List;

public interface TodoService {
    List<Todo> findAllTodosByUser(User user);
    Todo findTodoById(Long id);
    void saveOrUpdateTodo(Todo todo);
    void deleteTodoById(Long id);
}
