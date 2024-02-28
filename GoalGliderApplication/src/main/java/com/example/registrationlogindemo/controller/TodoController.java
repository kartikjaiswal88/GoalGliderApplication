package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.Todo;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.TodoRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TodoController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TodoRepository todoRepository;

    @RequestMapping("listtodos")
    public String listAllTodos(Model model) {
        // Get the currently logged-in user
        User user = getLoggedInUser();

        // Retrieve all todos associated with the user
        List<Todo> todos = user.getTodos();

        // Pass the todos to the view
        model.addAttribute("todos", todos);

        return "listtodos";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodoPage(Model model) {
        // Find the user by their email
        User user = getLoggedInUser();

        // Create a new Todo object and associate it with the user
        Todo todo = new Todo();
        todo.setDescription("Default description");
        todo.setTargetDate(LocalDate.now());
        todo.setDone(false);
        todo.setUser(user);

        // Pass the Todo object to the view
        model.addAttribute("todo", todo);

        return "todo";
    }


    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addNewTodo(ModelMap model, @Valid Todo todo, BindingResult result) {
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

        // Redirect to the listtodos page after saving the todo
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
