package com.example.registrationlogindemo.service;

import com.example.registrationlogindemo.entity.Todo;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public List<Todo> findAllTodosByUser(User user) {
        return user.getTodos();
    }

    @Override
    public Todo findTodoById(Long id) {
        return todoRepository.findById(id).orElse(null);
    }

    @Override
    public void saveOrUpdateTodo(Todo todo) {
        todoRepository.save(todo);
    }

    @Override
    @Transactional
    public void deleteTodoById(Long todoId) {
        // Perform the deletion logic
        todoRepository.deleteById(todoId);
    }

    // Other methods from TodoService interface if any
}

