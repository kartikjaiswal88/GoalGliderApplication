package com.example.registrationlogindemo.controller;

import com.example.registrationlogindemo.dto.EmailOtp;
import com.example.registrationlogindemo.dto.EmailRequest;
import com.example.registrationlogindemo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController("/email")
public class EmailController {

    @Autowired
    EmailService emailService;

    @RequestMapping("/email-welcome")
    public String welcome(){
        return "Welcome to email service";
    }

    @PostMapping("/sendmail")
    public String sendEmail(@RequestBody EmailRequest emailRequest){
        boolean f = emailService.sendEmail(emailRequest.getTo(),emailRequest.getSubject(),emailRequest.getMessage());
        if(f) {
            return "otp verified sent Successfully";
        }else{
            return "something went wrong";
        }
    }
    @GetMapping("/forget-password")
    public String forgetPassword(){

        return "redirect:forgetpassword";
    }

    @PostMapping("/send-email-verification")
    public String sendEmailVerify(@RequestBody EmailOtp map){
        System.out.println("sendEmailVerify called");
        boolean flag = emailService.sendEmailVerificationOtp(map.getEmail());
        if(flag){
            System.out.println("Email sent Successfully");
            return "Email sent Successfully";
        }
        return "something went wrong";
    }
    @PostMapping("/verify-otp")
    public String verifyEmail(@RequestBody EmailOtp emailOtp){
        boolean flag = emailService.verifyOtp(emailOtp.getEmail(),emailOtp.getOtp());
        if(flag){
            return "Otp verified Successfully";
        }
        return "Otp not invalid";
    }
}