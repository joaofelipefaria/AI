package br.com.joaofelipefaria.ai.springai.rag;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the RAG document ingestion pipeline.
 *
 * @param docsPath  folder containing the .txt files to embed and store
 * @param chunkSize target size (in tokens) of each chunk produced by the splitter
 */
@ConfigurationProperties(prefix = "app.rag")
public record RagProperties(

        String docsPath,
        int chunkSize

) {
}
