package dev.danvega.hub; 
import java.util.List;
import java.util.Map;
 
import org.springframework.ai.embedding.EmbeddingModel; 
import org.springframework.ai.embedding.EmbeddingResponse;  
import org.springframework.web.bind.annotation.*;  

@RestController 
public class EmbeddingController {
    
    private final EmbeddingModel embeddingModel;

    public EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }
    /***
     * https://docs.spring.io/spring-ai/reference/api/embeddings/ollama-embeddings.html#_sample_controller
     * */
    @GetMapping("/ai/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }
}
