package com.javaaidev.pdfqa;

import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

  @Bean
  public RetrievalAugmentationAdvisor questionAnswerAdvisor(
      VectorStore vectorStore) {
    return RetrievalAugmentationAdvisor.builder().documentRetriever(
        VectorStoreDocumentRetriever.builder().vectorStore(vectorStore)
            .build()
    ).build();
  }

  @Bean
  public SimpleLoggerAdvisor simpleLoggerAdvisor() {
    return new SimpleLoggerAdvisor();
  }

  @Bean
  public PDFContentLoader pdfContentLoader(VectorStore vectorStore) {
    return new PDFContentLoader(vectorStore);
  }
}
