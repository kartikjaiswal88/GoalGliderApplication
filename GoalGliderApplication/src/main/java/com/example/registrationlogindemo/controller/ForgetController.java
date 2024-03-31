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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@SessionAttributes("email")
public class ForgetController {
    private EmailService emailService;
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(ForgetController.class);

    public ForgetController(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @GetMapping("/forget-password")
    public String forgetPassword(){
        return "forget-password";
    }

    @PostMapping("/forget-password")
    public String sendOtp(@RequestParam Map<String,String> map, Model model, RedirectAttributes redirectAttributes){
        String email = map.get("email");
        logger.trace(email);
        User existingUser = userService.findUserByEmail(email);

        if(existingUser == null ){
            redirectAttributes.addFlashAttribute("error","Email not found");
            return "redirect:/forget-password";
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
    public String verifyOtp(@RequestParam Map<String,String> map, @SessionAttribute String email, RedirectAttributes redirectAttributes){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> entry : map.entrySet()){
            sb.append(entry.getValue());
        }

        Boolean flag = emailService.verifyOtp(email, sb.toString());
        if(!flag){
            redirectAttributes.addFlashAttribute("error","Please enter valid OTP");
            return "redirect:/verify-forgot-otp";
        }
        return "/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam Map<String,String> map, @SessionAttribute String email, SessionStatus sessionStatus, RedirectAttributes redirectAttributes){
        userService.updatePasswordForUser(email,map.get("floatingPassword"));
        sessionStatus.setComplete();
        redirectAttributes.addFlashAttribute("success","Password reset successfully !!!");
        return "redirect:/login";
    }
}
