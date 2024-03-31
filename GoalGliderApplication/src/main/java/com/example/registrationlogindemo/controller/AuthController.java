package com.example.registrationlogindemo.controller;
import java.util.List;
import java.util.Map;

import com.example.registrationlogindemo.service.EmailService;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.registrationlogindemo.dto.UserDto;
import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@SessionAttributes("userDto")
public class AuthController {

	private UserService userService;
    private EmailService emailService;

    public AuthController(UserService userService, EmailService emailService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    // handler method to handle home page request
    @GetMapping("/index")
    public String home(){
        return "index";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/verify")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }
        boolean flag = emailService.sendEmailVerificationOtp(userDto.getEmail());
        if(flag){
            log.debug("sending email verification to {}", userDto.getEmail());
            model.addAttribute("userDto",userDto);
            redirectAttributes.addFlashAttribute("success","Email sent successfully");
            return "verify-registration-email";
        }else{
            result.rejectValue("email", null,
                    "Invalid email");
                return "/register";
        }
//        userService.saveUser(userDto);
//        return "redirect:/register?success";
    }
    @PostMapping("/register/save")
    public String registerVerified(@SessionAttribute UserDto userDto, @RequestParam Map<String,String> map, SessionStatus sessionStatus, RedirectAttributes redirectAttributes){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> entry : map.entrySet()){
            sb.append(entry.getValue());
        }

        Boolean flag = emailService.verifyOtp(userDto.getEmail(), sb.toString());
        if(!flag){
            log.trace("otp not verified for user {} ", userDto.getEmail());
            redirectAttributes.addFlashAttribute("error","Please enter valid OTP");
            return "redirect:/register/save";
        }
        redirectAttributes.addFlashAttribute("success","You are succesfully registered");
        userService.saveUser(userDto);
        sessionStatus.setComplete();
        log.trace("user {} registered successfully", userDto.getEmail());
        return "redirect:/login";
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    // Handler method to handle the request of welcome page
    @GetMapping("/welcome")
    public String gotoWelcomePage() {
            return "welcome";

    }
}
