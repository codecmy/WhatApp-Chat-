package com.example.demo.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class TwilioWebhookValidator {

    @Value("${twilio.auth-token}")
    private String authToken;

    private static final String HMAC_SHA1 = "HmacSHA1";

    @PostConstruct
    public void init() {
        log.info("TwilioWebhookValidator initialized");
    }

    public boolean validate(String url, Map<String, String[]> parameters, String signature) {
        if (signature == null || signature.isEmpty()) {
            log.warn("No signature provided in webhook request");
            return false;
        }

        try {
            String expectedSignature = computeSignature(url, parameters);
            boolean isValid = signature.equals(expectedSignature);

            if (!isValid) {
                log.warn("Signature mismatch. Expected: {}, Received: {}", expectedSignature, signature);
            }

            return isValid;
        } catch (Exception e) {
            log.error("Error validating Twilio signature", e);
            return false;
        }
    }

    private String computeSignature(String url, Map<String, String[]> parameters) throws NoSuchAlgorithmException, InvalidKeyException {
        TreeMap<String, String> sortedParams = new TreeMap<>();
        parameters.forEach((key, values) -> {
            if (values != null && values.length > 0 && values[0] != null && !values[0].isEmpty()) {
                sortedParams.put(key, values[0]);
            }
        });

        StringBuilder sb = new StringBuilder(url);
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            sb.append(entry.getKey());
            sb.append(entry.getValue());
        }

        String data = sb.toString();

        Mac mac = Mac.getInstance(HMAC_SHA1);
        SecretKeySpec secretKeySpec = new SecretKeySpec(authToken.getBytes(StandardCharsets.UTF_8), HMAC_SHA1);
        mac.init(secretKeySpec);

        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    public String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return value;
        }
    }
}
