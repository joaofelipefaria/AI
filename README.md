# AI

AI engineering projects exploring Generative AI patterns for enterprise applications, built with **Spring AI** and a local, fully self-hosted LLM stack (Ollama + pgvector). Each project builds on the previous one, going from a plain chat endpoint to a full Retrieval Augmented Generation (RAG) pipeline.

## Projects

| Project | Description | Type |
|---|---|---|
| [`springai-firstsample`](./springai-firstsample) | Minimal Spring Boot + Spring AI chat endpoint backed by a local Ollama model. Starting point for the AI stack. | Runnable API (Docker + Maven) |
| [`springai-wRAG`](./springai-wRAG) | Full RAG pipeline: ingests local text documents, embeds them with `nomic-embed-text`, stores them in pgvector, and augments chat answers with retrieved context. | Runnable API (Docker + Maven) |

> The two projects share the same package layout (`br.com.joaofelipefaria.ai.springai`) — `springai-wRAG` is `springai-firstsample` extended with document ingestion and vector-store-backed retrieval.

## Technologies used across this repository

- **Language / Framework:** Java 17, Spring Boot 3.3.x, Spring AI 1.0.0 (BOM)
- **LLM runtime:** [Ollama](https://ollama.com/) (local model serving)
- **Models:** `llama3.2` / `llama3.1:8b-instruct-q4_0` (chat), `nomic-embed-text` (embeddings)
- **Vector store:** PostgreSQL + [pgvector](https://github.com/pgvector/pgvector) (`pgvector/pgvector:pg16` image)
- **Build tool:** Maven
- **Infra:** Docker / Docker Compose (separate compose files for the LLM runtime and the vector database)
- **Other:** Lombok, Spring Boot Actuator, Bean Validation

## How to run

Each project is self-contained. Start the required Docker services first (Ollama, and pgvector for the RAG project), then start the Spring Boot application with Maven. See each project's own README for exact commands:

- [springai-firstsample — setup & run](./springai-firstsample/README.md)
- [springai-wRAG — setup & run](./springai-wRAG/README.md)

## Author

João Felipe D'Assenção Faria — Software Architect / Lead Software Engineer.
