package dev.danvega.hub.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.danvega.hub.controller.SimpleRagController.RagRequest;
import dev.danvega.hub.controller.SimpleRagController.RagResponse;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html
 * **/
@RestController
@RequestMapping("/modular_rag")
public class ModularRagController {
   
    private final ChatClient chatClient;

    public ModularRagController(ChatClient.Builder builder,
        VectorStore vectorStore) {
      var ragAdvisor = RetrievalAugmentationAdvisor.builder()
          .documentRetriever(
            VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .topK(3)
                .build())
          .build();
      chatClient = builder.defaultAdvisors(ragAdvisor).build();
    }
 
    @PostMapping
    public RagResponse rag(@RequestBody RagRequest request) {
      var output = chatClient.prompt().user(request.input()).call().content();
      return new RagResponse(output);
    }
}