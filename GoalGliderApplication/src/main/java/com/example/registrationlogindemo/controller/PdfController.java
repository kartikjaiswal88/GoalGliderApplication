package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.Todo;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.repository.UserRepository;
import com.example.registrationlogindemo.service.PdfService;
import com.example.registrationlogindemo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.util.List;

@Controller
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService pdfService;
    @Autowired
    private TodoService todoService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/createPdf")
    public ResponseEntity<InputStreamResource> createPdf() {
        // Get the logged-in user's authentication details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Assuming the username is stored as the principal's name

        // Fetch todos for the logged-in user
        List<Todo> todos = getTodosForUser();

        ByteArrayInputStream pdf = pdfService.createPdf(todos, username);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "inline;filename=lcwd.pdf");

        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    // Method to fetch todos for the logged-in user (Replace with your actual implementation)
    private List<Todo> getTodosForUser() {
        return todoService.findAllTodosByUser(getLoggedInUser());
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
