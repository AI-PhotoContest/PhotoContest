package com.example.photocontest.controllers.mvc;

import com.example.photocontest.services.EmailServiceNewsLetter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NewsletterController {

    private final EmailServiceNewsLetter emailService;

    public NewsletterController(EmailServiceNewsLetter emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/newsletter/subscribe")
    public String subscribeToNewsletter(@RequestParam("email") String email, Model model) {
        try {
            emailService.sendNewsletterEmail(email);
            model.addAttribute("message", "Successfully subscribed to the newsletter!");
        } catch (Exception e) {
            model.addAttribute("message", "There was an error. Please try again.");
        }
        return "redirect:/home";
    }
}