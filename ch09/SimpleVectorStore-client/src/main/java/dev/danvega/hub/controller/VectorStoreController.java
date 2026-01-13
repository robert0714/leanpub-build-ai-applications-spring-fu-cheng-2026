package dev.danvega.hub.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.danvega.hub.service.VectorStoreService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;

import org.springframework.util.CollectionUtils;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/vector_store")
public class VectorStoreController {

  private final VectorStoreService vectorStoreService;

  public VectorStoreController(VectorStoreService vectorStoreService) {
    this.vectorStoreService = vectorStoreService;
  }

  @PostMapping("/document")
  public ResponseEntity<Void> add(@RequestBody Document document) {
    String documentId = vectorStoreService.add(document);
    String createdUri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{documentId}").buildAndExpand(documentId).toUriString();
    return ResponseEntity.created(URI.create(createdUri)).build();
  }

  @GetMapping("/document/{documentId}")
  public ResponseEntity<Document> getById(
      @PathVariable("documentId") String documentId) {
    return vectorStoreService.getById(documentId).map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @DeleteMapping("/document/{documentId}")
  public void delete(@PathVariable("documentId") String documentId) {
    vectorStoreService.delete(documentId);
  }

  @PostMapping("/query")
  public List<Document> query(@RequestBody QueryRequest queryRequest) {
    return vectorStoreService.query(queryRequest.query(), queryRequest.metadataFilter());
  }
  
  // 自定義 DTO 來接收查詢請求
  public record QueryRequest(
      String query,
      String metadataFilter,
      Integer topK
  ) {
    public QueryRequest {
      if (topK == null) {
        topK = 5; // 預設值
      }
    }
  }
}