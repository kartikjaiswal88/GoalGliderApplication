package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.Todo;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.TodoRepository;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

@Controller
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("listtodos")
    public String listAllTodos(Model model) {
        User user = getLoggedInUser();
        model.addAttribute("todos", todoService.findAllTodosByUser(user));
        return "listtodos";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodoPage(Model model) {
        User user = getLoggedInUser();
        Todo todo = new Todo();
        todo.setDescription("");
        todo.setTargetDate(LocalDate.now());
        todo.setDone(false);
        todo.setUser(user);
        model.addAttribute("todo", todo);
        return "todo";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addNewTodo(@Valid @ModelAttribute Todo todo, BindingResult result) {
        if (todo.getTargetDate() != null && todo.getTargetDate().isBefore(LocalDate.now())) {
            result.rejectValue("targetDate", "error.todo", "Target date must be in the future");
        }
        if (todo.getDescription().length() < 8 || todo.getDescription().length() > 30) {
            result.rejectValue("description", "error.todo", "Description must be between 8 and 30 characters");
        }
        if (result.hasErrors()) {
            return "todo";
        }
        User user = getLoggedInUser();
        todo.setUser(user);
        todoRepository.save(todo);
        return "redirect:/listtodos";
    }

    @PostMapping("/delete-todo")
    public String deleteTodo(@RequestParam("todoId") Long todoId) {
        todoService.deleteTodoById(todoId);
        return "redirect:/listtodos";
    }

    @GetMapping("/update-todo")
    public String showUpdateTodoForm(@RequestParam("todoId") Long todoId, Model model) {
        Todo todo = todoService.findTodoById(todoId);
        model.addAttribute("todo", todo);
        return "update-todo";
    }

    @PostMapping("/update-todo")
    public String updateTodo(@Valid Todo todo, BindingResult result) {
        if (result.hasErrors()) {
            return "update-todo";
        }
        User user = getLoggedInUser();
        todo.setUser(user);
        todoService.saveOrUpdateTodo(todo);
        return "redirect:/listtodos";
    }

    @PostMapping("/mark-as-done")
    public String markTodoAsDone(@RequestParam("todoId") Long todoId) {
        // Find the todo by its ID
        Todo todo = todoService.findTodoById(todoId);

        // Mark the todo as done
        todo.setDone(true);

        // Save the updated todo
        todoService.saveOrUpdateTodo(todo);

        // Redirect to the listtodos page after marking the todo as done
        return "redirect:/listtodos";
    }

    @PostMapping("/start-stopwatch/{todoId}")
    public String startStopwatch(@PathVariable Long todoId) {
        Todo todo = todoRepository.findById(todoId).orElse(null);
        if (todo != null) {
            Instant now = Instant.now();
            Instant stopwatchStartTime = todo.getStopwatchStartTime();
            if (stopwatchStartTime == null) {
                // Start the stopwatch
                todo.setStopwatchStartTime(now);
            } else {
                // Stop the stopwatch and calculate elapsed time
                long elapsedTimeMillis = Duration.between(stopwatchStartTime, now).toMillis();
                todo.setTotalTime(todo.getTotalTime() + elapsedTimeMillis);
                todo.setStopwatchStartTime(null);
            }
            todoRepository.save(todo);
        }
        return "redirect:/listtodos";
    }

    @GetMapping("/pieChart")
    public String showPieChart() {
        return "pieChart";
    }


    private User getLoggedInUser() {
        String email = getLoggedInUsername();
        return userRepository.findByEmail(email);
    }

    private String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
