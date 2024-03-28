package com.example.registrationlogindemo.service;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailService {

    static Map<String,String> emailToOtp = new HashMap<String,String>();

    public boolean sendEmailVerificationOtp(String email){
        String subject = "OTP for Email Verification";
        String otp = generateOtp();
        String message = "<div style='border:1px solid #e2e2e2; padding:20px'>" +

                "<h1>OTP : " + otp +
                "</h1></div>";

        emailToOtp.put(email, otp);
        return sendEmail(email, subject, message);
    }

    public boolean verifyOtp(String email, String otp){
        boolean isVerified;
        isVerified = emailToOtp.get(email).equals(otp);
        if(isVerified){
            emailToOtp.remove(email);
            System.out.println("otp verified");
            return true;
        }else{
            return false;
        }
    }

    public boolean sendEmail(String to, String subject, String text){
        boolean flag = false;
        String from = "kartiksahu81713@gmail.com";
        //set properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth",true);
        properties.put("mail.smtp.starttls.enable",true);
        properties.put("mail.smtp.port",587);
        properties.put("mail.smtp.host","smtp.gmail.com");

        String username ="kartiksahu81713";
        String password = "wlhc ndcd tcyu edfc";

        //get Session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication (username, password);
            }
        });

        try{
            Message message = new MimeMessage(session);

            message.setRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);
            message.setContent(text,"text/html");


            Transport.send(message);

            flag = true;

        }catch(Exception e){
            e.printStackTrace();
            flag = false;
        }

        return true;
    }

    public boolean sendEmail(String to, String subject, String text, ByteArrayInputStream byteArrayInputStream) {
        boolean flag = false;
        String from = "kartiksahu81713@gmail.com";
        // Set properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.host", "smtp.gmail.com");

        String username = "kartiksahu81713";
        String password = "wlhc ndcd tcyu edfc";

        // Get Session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setFrom(new InternetAddress(from));
            message.setSubject(subject);

            // Create multipart content
            MimeMultipart multipart = new MimeMultipart();

            // Create text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(text, "utf-8", "html");
            multipart.addBodyPart(textPart);

            // Create PDF attachment part
            MimeBodyPart attachmentPart = new MimeBodyPart();
            try {
                DataSource source = new ByteArrayDataSource(byteArrayInputStream,"application/pdf");
                attachmentPart.setDataHandler(new DataHandler(source));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            attachmentPart.setFileName("generated_pdf.pdf");
            multipart.addBodyPart(attachmentPart);

            // Set content
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            flag = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return flag;
    }
    public String generateOtp(){
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}