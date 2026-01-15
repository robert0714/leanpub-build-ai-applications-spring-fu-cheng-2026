package com.javaaidev.pdfqa;

import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.FileSystemResource;

public class PDFContentLoader implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(
      PDFContentLoader.class);
  private final VectorStore vectorStore;

  public PDFContentLoader(VectorStore vectorStore) {
    this.vectorStore = vectorStore;
  }

  public void load(Path pdfFilePath) {
    LOGGER.info("Load PDF file {}", pdfFilePath);
    var reader = new PagePdfDocumentReader(
        new FileSystemResource(pdfFilePath));
    var splitter = new TokenTextSplitter();
    var docs = splitter.split(reader.read());
    vectorStore.add(docs);
    LOGGER.info("Loaded {} docs", docs.size());
  }

  @Override
  public void run(String... args) throws Exception {
    var markerFile = Path.of(".", ".pdf-imported");
    if (Files.exists(markerFile)) {
      LOGGER.info(
          "Marker file {} exists, skip. Delete this file to re-import.",
          markerFile);
      return;
    }
    load(Path.of(".", "content", "Understanding_Climate_Change.pdf"));
    Files.createFile(markerFile);
  }
}
