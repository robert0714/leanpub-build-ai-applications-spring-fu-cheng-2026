package com.javaaidev.springai.agent.cooking;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cooking")
public class CookingAgentController {

  private static final String SYSTEM_TEXT = """
      You are a chef who is proficient in various cuisines.
      Please answer users' questions about cooking.
      For other unrelated inputs, simply tell the user that you don't know.
      """;

  private final ChatClient chatClient;

  public CookingAgentController(ChatClient.Builder builder) {
    chatClient = builder.build();
  }

  @PostMapping
  public AgentResponse chat(@RequestBody AgentRequest request) {
    return new AgentResponse(
        chatClient.prompt().system(SYSTEM_TEXT)
            .user(request.input())
            .call().content());
  }
}
