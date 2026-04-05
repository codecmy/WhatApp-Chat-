package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WebhookRequest {
    @JsonProperty("From")
    private String from;

    @JsonProperty("Body")
    private String body;

    @JsonProperty("To")
    private String to;

    @JsonProperty("MessageSid")
    private String messageSid;
}
