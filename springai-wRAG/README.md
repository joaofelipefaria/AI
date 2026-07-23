# Spring AI — RAG Pipeline

A Spring Boot application implementing a full **Retrieval Augmented Generation (RAG)** pipeline on top of Spring AI: local documents are chunked, embedded, and stored in a pgvector database, then retrieved and injected as context for chat answers backed by a local Ollama model.

This project extends [`springai-firstsample`](../springai-firstsample) with document ingestion and vector-store-backed retrieval.

## What it does

- On startup, `DocumentIngestionRunner` reads text documents from `doc-rag/` (e.g. `example-company-policy.txt`, `example-product-faq.txt`), splits them into chunks, embeds them with the `nomic-embed-text` model, and stores them in a PostgreSQL/pgvector vector store.
- The chat endpoint retrieves the most relevant chunks for a given question (via a vector-store advisor) and injects them into the prompt sent to the Ollama chat model, so answers are grounded in the ingested documents rather than the model's own knowledge alone.
- Same layered architecture, exception handling, and DTOs as `springai-firstsample`, plus `rag/DocumentIngestionRunner` and `rag/RagProperties`.

## Architecture

```
doc-rag/*.txt → DocumentIngestionRunner → embeddings (nomic-embed-text) → pgvector

Client → ChatController → ChatService → Spring AI ChatClient (+ vector-store advisor) → Ollama
                                                     ↑
                                          retrieved context (pgvector)
```

## Technologies used

- Java 17
- Spring Boot 3.3.5
- Spring AI 1.0.0 (BOM):
  - `spring-ai-starter-model-ollama`
  - `spring-ai-starter-vector-store-pgvector`
  - `spring-ai-advisors-vector-store`
- Ollama (local LLM runtime) — chat model `llama3.2`, embedding model `nomic-embed-text`
- PostgreSQL + pgvector (`pgvector/pgvector:pg16`) as the vector store, HNSW index, cosine distance
- Spring Boot Actuator, Bean Validation, Lombok
- Maven
- Docker / Docker Compose (two separate compose files: one for Ollama, one for the vector database)

## How to run

The Spring Boot application itself runs on the host/IDE via Maven. Two independent Docker Compose stacks provide its dependencies: the LLM runtime and the vector database.

### 1. Start the LLM runtime (Ollama)

```bash
cd docker-llm
docker compose up -d
```
Starts `ollama` on port `11434` and auto-pulls the `llama3.2` model on first run.

### 2. Start the vector database (pgvector)

```bash
cd ../docker-vectordb
docker compose up -d
```
Starts PostgreSQL + pgvector on port `5432`, database `vectordb` (user/password: `vectordb`).

### 3. Start the application

```bash
mvn spring-boot:run
```

On startup, the application connects to `localhost:11434` (Ollama) and `localhost:5432` (pgvector), auto-creates the vector store schema (`initialize-schema: true`), and ingests the documents found in `app.rag.docs-path` (`./doc-rag` by default, chunk size 800).

### 4. Ask a grounded question

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What does the company policy say about remote work?"}'
```

The answer will be grounded in the content of `doc-rag/example-company-policy.txt` / `example-product-faq.txt`, using the top-k (4) most similar chunks above the configured similarity threshold (0.5).

> If you containerize the Spring Boot app itself, activate the `docker` profile (`application-docker.yml`) to point at `http://ollama:11434` instead of `localhost`.

### Key configuration (`application.yml`)

| Setting | Value | Meaning |
|---|---|---|
| `spring.ai.ollama.chat.model` | `llama3.2` | Chat model |
| `spring.ai.ollama.embedding.model` | `nomic-embed-text` | Embedding model (768 dimensions) |
| `spring.ai.vectorstore.pgvector.top-k` | `4` | Chunks retrieved per question |
| `spring.ai.vectorstore.pgvector.similarity-threshold` | `0.5` | Minimum similarity to use a chunk |
| `app.rag.docs-path` | `./doc-rag` | Folder ingested at startup |
| `app.rag.chunk-size` | `800` | Chunk size in characters |
