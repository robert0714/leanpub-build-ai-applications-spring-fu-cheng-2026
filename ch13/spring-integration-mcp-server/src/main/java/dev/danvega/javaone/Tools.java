package dev.danvega.javaone;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.LoggingLevel;
import io.modelcontextprotocol.spec.McpSchema.LoggingMessageNotification;

public class Tools {
	public static final McpServerFeatures.SyncToolSpecification 
	CALCULATE_HTTP_BASIC_AUTH_HEADER 
	 = new McpServerFeatures.SyncToolSpecification(
			new McpSchema.Tool(
					"calculateHttpBasicAuthHeader",
					"Calculate value of HTTP Authorization header for basic auth",
					new McpSchema.JsonSchema("object",
							Map.of("username",
									new McpSchema.JsonSchema("string", Map.of(), List.of(), 
											false, Map.of(), Map.of()),
									"password",
									new McpSchema.JsonSchema("string", Map.of(), List.of(),
											false, Map.of(), Map.of())),
							List.of("username", "password"), 
							false, Map.of(), Map.of())),
			(exchange, args) -> {
				String username = (String) args.get("username");
				String password = (String) args.get("password");
				String header = 
						"Basic " + Base64.getEncoder()
						.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
				
				exchange.loggingNotification(new LoggingMessageNotification(
						LoggingLevel.INFO,
						"http-basic-auth",
						"""
					    	{"username": "%s"}
						    """.formatted(username)
				));
				return new CallToolResult(header, false);
			});
}
