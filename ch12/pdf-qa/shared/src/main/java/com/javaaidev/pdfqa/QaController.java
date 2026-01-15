package com.javaaidev.pdfqa;

import com.javaaidev.chatagent.model.ChatAgentRequest;
import com.javaaidev.chatagent.model.ChatAgentResponse;
import com.javaaidev.chatagent.springai.ModelAdapter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class QaController {

  private final ChatClient chatClient;

  public QaController(ChatClient.Builder builder,
      RetrievalAugmentationAdvisor ragAdvisor,
      SimpleLoggerAdvisor simpleLoggerAdvisor) {
    this.chatClient = builder.defaultAdvisors(
        ragAdvisor,
        simpleLoggerAdvisor).build();
  }

  @PostMapping("/chat")
  public Flux<ServerSentEvent<ChatAgentResponse>> qa(
      @RequestBody ChatAgentRequest request) {
    return ModelAdapter.toStreamingResponse(
        chatClient.prompt()
            .messages(
                ModelAdapter.fromRequest(request).toArray(new Message[0]))
            .stream()
            .chatResponse());
  }

}
