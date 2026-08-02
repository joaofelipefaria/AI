# ACME Chat Spring

This project is a Spring Boot application that exposes a simple chat API backed by Spring AI and an Ollama LLM. It is designed as a practical example of a conversational AI agent that can be extended with tool use and retrieval-augmented generation (RAG).

## Purpose

The application is structured as a lightweight HR assistant example. The system prompt is configured so the model behaves as an HR assistant that:

- acts as a helpful HR support assistant
- uses available tools to consult employee leave balances and employee data
- uses RAG context to consult company leave policies
- does not invent or fabricate information

## Architecture

The project is intentionally small and focused on clarity:

- Spring Boot web layer exposes a REST endpoint
- Spring AI ChatClient integrates with an Ollama model
- a simple service layer handles prompt execution and error handling
- configuration is externalized in application.yml

## Main components

- ChatController: exposes the REST API
- ChatService: orchestrates the request to the language model
- ChatClientConfig: defines the default system prompt and chat client bean
- application.yml: runtime configuration for the Ollama model and server

## System prompt

The system prompt is configured to guide the model toward a safe and accurate HR assistant behavior:

You are an HR assistant. You must consult employee leave information and employee data through the available tools. You must also consult company leave policies from the RAG context. Do not invent data. If the information is missing, clearly say you cannot confirm it.

## Prerequisites

Before running this project, make sure you have:

- Java 17+
- Maven 3.8+
- Ollama installed and running locally
- an Ollama model available, for example llama3.2

## Running the application

1. Start Ollama locally.
2. Make sure the model exists. For example:

   ```bash
   ollama pull llama3.2
   ```

3. Run the application:

   ```bash
   mvn spring-boot:run
   ```

4. The application will start on port 8080.

## API usage

Send a request to the chat endpoint:

```bash
curl -X POST http://localhost:8080/api/v1/chat \
  -H "Content-Type: application/json" \
  -d '{"question":"What is the leave balance for employee 101?"}'
```

Example response:

```json
{
  "answer": "...",
  "model": "llama3.2"
}
```

## Suggested next steps

To make this agent behave more like a real MCP-powered HR assistant, the next improvements could be:

- add MCP tools for employee and leave lookups
- add a RAG knowledge base with company leave policies
- add structured tool calling and validation
- add integration tests for the chat flow
