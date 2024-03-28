package com.example.registrationlogindemo.scheduler;

import com.example.registrationlogindemo.entity.User;
import com.example.registrationlogindemo.service.EmailService;
import com.example.registrationlogindemo.service.PdfService;
import com.example.registrationlogindemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TodoReminderScheduler {
    EmailService emailService;
    PdfService pdfService;
    UserService userService;
    public TodoReminderScheduler(EmailService emailService, UserService userService, PdfService pdfService) {
        this.emailService = emailService;
        this.userService = userService;
        this.pdfService = pdfService;
    }

    private static final Logger logger = LoggerFactory.getLogger(TodoReminderScheduler.class);

    @Scheduled(cron = " 0 54 23 * * *")
    public void todoReminederScheduler(){
        logger.info(" ");
        for (User user : userService.findAllUser()) {
            System.out.println(user);
//            for (Todo todo:user.getTodos()) {
//                System.out.println(todo);
//            }
            if(user.getTodos().size() > 0)
                emailService.sendEmail(user.getEmail(),"task analysis","task done by you are",pdfService.createPdf(user.getTodos(),user.getEmail() ));
        }
    }
}
