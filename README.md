# AI

This section gathers experiments around Spring AI, the Model Context Protocol (MCP), and retrieval-augmented generation. The projects are mostly self-contained and can be explored independently.

## Projects

- springai-firstsample: a minimal Spring Boot + Spring AI chat endpoint backed by a local Ollama model.
- springai-mcp: a set of examples around MCP servers, clients, and supporting APIs.
- springai-wRAG: a complete RAG pipeline that ingests documents, stores embeddings in pgvector, and augments chat responses.

## Technologies used across this repository

- Java 17+
- Spring Boot
- Spring AI
- Maven
- Docker / Docker Compose
- Ollama and pgvector for local LLM and vector-store experiments

## How to explore

Start with springai-firstsample for the basic flow, then move to springai-mcp or springai-wRAG for more advanced integrations.

