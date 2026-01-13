package dev.danvega.hub.controller; 
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; 


import io.swagger.v3.oas.annotations.media.Schema;
/**
 * https://docs.spring.io/spring-ai/reference/api/retrieval-augmented-generation.html
 * **/
@RestController
@RequestMapping("/simple_rag")
public class SimpleRagController {
   private final ChatClient chatClient;
   private final VectorStore vectorStore;
 
   private static final String USER_PROMPT_TEMPLATE = """
       Answer the question using provided content:
       
       Question: {question}
       
       Provided content: {content}
       
       Answer:
       """;
 
   public SimpleRagController(ChatClient.Builder builder,
       VectorStore vectorStore, SimpleLoggerAdvisor simpleLoggerAdvisor) {
       this.vectorStore = vectorStore;
       chatClient = builder.defaultAdvisors(simpleLoggerAdvisor).build();
   }
 
	@PostMapping
	public ResponseEntity<RagResponse> rag(@RequestBody RagRequest request) {
		List<Document> docs = vectorStore
				.similaritySearch(SearchRequest.builder().query(request.input()).topK(1).build());
		if (docs == null) {
			return ResponseEntity.internalServerError().build();
		}
		var content = docs.stream().map(Document::getText).collect(Collectors.joining("\n\n"));
		var output = chatClient.prompt()
				.user(userSpec -> userSpec
						.text(USER_PROMPT_TEMPLATE)
						.param("content", content)
				        .param("question", request.input()))
				.call().content();
		return ResponseEntity.ok(new RagResponse(output));
	}
	 // Custom DTO to receive query requests
	public record RagRequest(
		@Schema(description = "Input query for retrieval" , example = "how many continents are there on Earth?")
		String input) {
		public RagRequest {
		}
	}

	public record RagResponse(
		@Schema(description = "Output answer for the input query" , example = "There are seven continents on Earth.")
		String output) {
		public RagResponse {
		}
	}
}
