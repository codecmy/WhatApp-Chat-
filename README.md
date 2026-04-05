# WhatApp Chat

A Spring Boot application for WhatsApp messaging integration using Twilio.

## Tech Stack

- **Java 17**
- **Spring Boot 4.0.5**
- **Twilio SDK 10.9.0**
- **Lombok**

## Setup

1. Configure your Twilio credentials in `src/main/resources/application.yaml`:
   ```yaml
   twilio:
     account-sid: YOUR_ACCOUNT_SID
     auth-token: YOUR_AUTH_TOKEN
     from-number: YOUR_WHATSAPP_NUMBER
   ```

2. Set up your webhook URL in Twilio to point to `/webhook`

## Build & Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/health-check` | Health check endpoint |
| POST | `/webhook` | Twilio webhook for incoming messages |

## Project Structure

```
src/main/java/com/example/demo/
├── DemoApplication.java       # Main application entry
├── controller/
│   └── WhatAppcontroller.java # Webhook handler
├── services/
│   ├── ChatServices.java      # Message processing logic
│   └── TwilioWebhookValidator.java
└── DTO/
    └── WebhookRequest.java
```

## License

MIT
