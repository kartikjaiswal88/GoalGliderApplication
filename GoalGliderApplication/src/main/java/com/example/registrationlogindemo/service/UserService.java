package com.example.registrationlogindemo.service;

import java.util.List;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.Todo;
import com.example.registrationlogindemo.entity.User;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();

    void addTodoToUser(User user, Todo todo);
    void deleteTodoFromUser(User user, Todo todo);
    void updateTodoForUser(User user, Todo todo);
    void updatePasswordForUser(String email, String password);
}
