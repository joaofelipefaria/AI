# Spring AI — First Sample (Ollama Chat)

A minimal Spring Boot application that exposes a REST chat endpoint backed by a locally-hosted LLM via **Spring AI** and **Ollama**. This is the starting point of the AI projects in this repository — no vector store, no RAG, just a clean chat client wired end-to-end.

## What it does

- Exposes a chat endpoint that forwards the user's message to a local Ollama model and returns the model's response.
- Uses Spring AI's `ChatClient` abstraction, configured in `ChatClientConfig`.
- Includes centralized exception handling (`GlobalExceptionHandler`, `ChatServiceException`) and request/response DTOs.
- Exposes Actuator health/info endpoints for production-readiness.

## Architecture

```
Client → ChatController → ChatService (ChatServiceImpl) → Spring AI ChatClient → Ollama
```

- `controller/ChatController` — REST entry point
- `service/ChatService` + `service/impl/ChatServiceImpl` — business logic / orchestration
- `config/ChatClientConfig` — Spring AI `ChatClient` bean configuration
- `dto/ChatRequest`, `dto/ChatResponse`, `dto/ErrorResponse` — API contracts
- `exception/` — centralized error handling

## Technologies used

- Java 17
- Spring Boot 3.3.5
- Spring AI 1.0.0 (BOM) — `spring-ai-starter-model-ollama`
- Ollama (local LLM runtime), model: `llama3.2`
- Spring Boot Actuator
- Bean Validation
- Lombok
- Maven
- Docker / Docker Compose (for the Ollama runtime only)

## How to run

The application itself runs on the host/IDE via Maven; only the Ollama LLM runtime is containerized.

### 1. Start Ollama (and auto-pull the model)

```bash
cd docker
docker compose up -d
```

This starts the `ollama` service on port `11434` and a one-shot `ollama-model-pull` container that pulls `llama3.2` automatically the first time.

### 2. Start the application

```bash
mvn spring-boot:run
```

The application will connect to Ollama at `http://localhost:11434` (see `application.yml`).

### 3. Call the chat endpoint

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello, who are you?"}'
```

> If you later run the Spring Boot app itself inside Docker on the same network as Ollama, activate the `docker` profile (`application-docker.yml`), which points to `http://ollama:11434` instead of `localhost`.

### Notes

- `OLLAMA_KEEP_ALIVE=24h` keeps the model loaded in memory to avoid cold-start latency between requests.
- Uncomment the GPU section in `docker/docker-compose.yml` if you have the NVIDIA Container Toolkit installed and want GPU acceleration.
