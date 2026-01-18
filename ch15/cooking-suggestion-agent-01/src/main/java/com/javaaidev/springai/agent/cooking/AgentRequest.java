package com.javaaidev.springai.agent.cooking;

import io.swagger.v3.oas.annotations.media.Schema;

public record AgentRequest(@Schema(description = "Input query for retrieval" , example = "cook fish") String input) {

}
