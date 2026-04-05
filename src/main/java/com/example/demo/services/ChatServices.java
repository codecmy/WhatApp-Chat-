package com.example.demo.services;

import org.springframework.stereotype.Service;

@Service
public class ChatServices {
    public String processMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Please send a message";
        }

        String normalizedMessage = message.trim().toUpperCase();

        return switch (normalizedMessage) {
            case "HI" -> "Hello";
            case "BYE" -> "Goodbye";
            default -> "I didn't understand that. Send HI or BYE";
        };
    }
}
