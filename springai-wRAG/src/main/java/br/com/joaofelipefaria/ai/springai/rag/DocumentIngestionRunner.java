package br.com.joaofelipefaria.ai.springai.rag;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * On application startup, reads every *.txt file under {@code app.rag.docs-path}
 * (the "doc-rag" folder), splits it into chunks and stores the embeddings in
 * the pgvector store, tagging each chunk with its source file name.
 * <p>
 * Ingestion is idempotent: a file already represented in the vector store
 * (matched by its "source" metadata) is skipped on subsequent restarts, so
 * you can freely add new .txt files to the folder and just restart the app.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentIngestionRunner implements ApplicationRunner {

    private static final String SOURCE_METADATA_KEY = "source";

    private final VectorStore vectorStore;
    private final RagProperties ragProperties;

    @Override
    public void run(ApplicationArguments args) throws IOException {
        Path docsDir = Path.of(ragProperties.docsPath());

        if (!Files.isDirectory(docsDir)) {
            log.warn("RAG docs folder [{}] not found - skipping ingestion.", docsDir.toAbsolutePath());
            return;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(docsDir, "*.txt")) {
            for (Path file : stream) {
                ingestIfNeeded(file);
            }
        }
    }

    private void ingestIfNeeded(Path file) {
        String fileName = file.getFileName().toString();

        if (alreadyIngested(fileName)) {
            log.info("RAG document [{}] already present in the vector store - skipping.", fileName);
            return;
        }

        log.info("Ingesting RAG document [{}]...", fileName);

        TextReader reader = new TextReader(new FileSystemResource(file));
        reader.getCustomMetadata().put(SOURCE_METADATA_KEY, fileName);

        List<Document> rawDocuments = reader.get();

        TokenTextSplitter splitter = new TokenTextSplitter(
                ragProperties.chunkSize(), 350, 5, 10000, true);
        List<Document> chunks = splitter.apply(rawDocuments);

        vectorStore.add(chunks);

        log.info("Ingested {} chunk(s) from [{}].", chunks.size(), fileName);
    }

    /**
     * Checks whether any chunk tagged with this file name already exists in
     * the vector store, so a restart never re-embeds the same file twice.
     */
    private boolean alreadyIngested(String fileName) {
        List<Document> existing = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(fileName)
                        .topK(1)
                        .filterExpression(SOURCE_METADATA_KEY + " == '" + fileName + "'")
                        .build());

        return !existing.isEmpty();
    }

}
