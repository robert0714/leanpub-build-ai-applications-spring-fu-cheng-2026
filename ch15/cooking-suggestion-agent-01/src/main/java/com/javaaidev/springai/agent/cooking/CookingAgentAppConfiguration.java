package com.javaaidev.springai.agent.cooking;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.ai.util.JacksonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookingAgentAppConfiguration {

  @Bean
  public ObjectMapper objectMapper() {
    return JsonMapper.builder()
        .addModules(JacksonUtils.instantiateAvailableModules())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build();
  }


}
