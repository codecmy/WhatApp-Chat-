package com.example.demo.controller;

import com.example.demo.services.ChatServices;
import com.example.demo.services.TwilioWebhookValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WhatAppcontroller {

    private final TwilioWebhookValidator webhookValidator;
    private final ChatServices chatServices;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "healthy";
    }

    @PostMapping(value = "/webhook", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> webhook(
            @RequestParam(required = false) String From,
            @RequestParam(required = false) String Body,
            @RequestParam(required = false) String To,
            @RequestParam(required = false) String MessageSid,
            @RequestParam(required = false) String MessageStatus) {

        log.info("=== INCOMING TWILIO REQUEST ===");
        log.info("From: {}", From);
        log.info("Body: {}", Body);
        log.info("MessageStatus: {}", MessageStatus);
        log.info("================================");

        if (MessageStatus != null) {
            log.info("Received status callback: {}", MessageStatus);
            return ResponseEntity.ok("<Response></Response>");
        }

        if (Body == null || Body.trim().isEmpty()) {
            log.warn("Body is null → likely not a user message");
            return ResponseEntity.ok("<Response></Response>");
        }

        log.info("=== INCOMING WHATSAPP MESSAGE ===");
        log.info("From: {}", From);
        log.info("To: {}", To);
        log.info("Body: {}", Body);
        log.info("MessageSid: {}", MessageSid);
        log.info("================================");

        String responseMessage = chatServices.processMessage(Body);

        log.info("Sending response: {}", responseMessage);

        return ResponseEntity.ok("<Response><Message>" + responseMessage + "</Message></Response>");
    }
}
