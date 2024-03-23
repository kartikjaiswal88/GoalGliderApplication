package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.EmailService;
import com.example.registrationlogindemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;

@Controller
@SessionAttributes("email")
public class ForgetController {
    private EmailService emailService;
    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(ForgetController.class);

    public ForgetController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping("/forget-password")
    public String forgetPassword(){
        return "forget-password";
    }

    @PostMapping("/forget-password")
    public String sendOtp(@RequestParam Map<String,String> map, Model model){
        String email = map.get("email");
        logger.trace(email);
        User existingUser = userService.findUserByEmail(email);

        if(existingUser == null ){
            return "redirect:/forget-password?notfound";
        }
        model.addAttribute("email", email);
        Boolean flag = emailService.sendEmailVerificationOtp(email);
        logger.trace("redirecting to /verify-forget-otp");
        return "redirect:/verify-forgot-otp";
    }
    @GetMapping("/verify-forgot-otp")
    public String verifyForgototp(){
        return "verify-forgot-otp";
    }
    @PostMapping("/verify-forgot-otp")
    public String verifyOtp(@RequestParam Map<String,String> map, @SessionAttribute String email){
        Boolean flag = emailService.verifyOtp(email, map.get("otp"));
        if(!flag){
            return "redirect:/verify-forgot-otp?invalidOtp";
        }
        return "/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam Map<String,String> map, @SessionAttribute String email, SessionStatus sessionStatus){
        userService.updatePasswordForUser(email,map.get("new-password"));
        sessionStatus.setComplete();
        return "redirect:/login?passwordReset";
    }
}
