package dev.danvega.hub.controller;

 
import java.net.URI;
import java.util.List;
 
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.danvega.hub.service.VectorStoreService;
 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.ai.document.Document; 

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