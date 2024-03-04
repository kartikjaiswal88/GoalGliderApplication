package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.Todo;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.TodoRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.TodoService;
import com.example.registrationlogindemo.service.TodoServiceImpl;
import com.example.registrationlogindemo.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TodoController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @RequestMapping("listtodos")
    public String listAllTodos(Model model) {
        // Get the currently logged-in user
        User user = getLoggedInUser();
        model.addAttribute("todos",todoService.findAllTodosByUser(user));

        return "listtodos";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodoPage(Model model) {
        // Find the user by their email
        User user = getLoggedInUser();

        // Create a new Todo object and associate it with the user
        Todo todo = new Todo();
        todo.setDescription("");
        todo.setTargetDate(LocalDate.now());
        todo.setDone(false);
        todo.setUser(user);

        // Pass the Todo object to the view
        model.addAttribute("todo", todo);

        return "todo";
    }


    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addNewTodo(@Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "todo"; // Return to the todo form if there are validation errors
        }

        // Get the currently logged-in user
        User user = getLoggedInUser();

        // Set the user for the todo
        todo.setUser(user);

        // Save the todo
        todoRepository.save(todo);

        System.out.println("Saving..."+ todo.toString());

//        userServiceImpl.addTodoToUser(user,todo); Check it out

        // Redirect to the listtodos page after saving the todo
        return "redirect:/listtodos";
    }


    @PostMapping("/delete-todo")
    public String deleteTodo(@RequestParam("todoId") Long todoId) {
        // Call the service method to delete the todo
        todoService.deleteTodoById(todoId);

        // Redirect to the desired page after deletion
        return "redirect:/listtodos";
    }

    @GetMapping("/update-todo")
    public String showUpdateTodoForm(@RequestParam("todoId") Long todoId, Model model) {
        Todo todo = todoService.findTodoById(todoId);
        model.addAttribute("todo", todo);
        return "update-todo"; // Return the update form view
    }

    @PostMapping("/update-todo")
    public String updateTodo(@Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "update-todo"; // Return to the update form if there are validation errors
        }

        // Get the currently logged-in user
        User user = getLoggedInUser();

        // Set the user for the todo
        todo.setUser(user);

        // Save the updated todo
        todoService.saveOrUpdateTodo(todo);

        // Redirect to the listtodos page after updating the todo
        return "redirect:/listtodos";
    }








    private User getLoggedInUser() {
        // Retrieve the currently logged-in user's email
        String email = getLoggedInUsername();

        // Find the user by their email
        User user = userRepository.findByEmail(email);

        return user;
    }

    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }


}
