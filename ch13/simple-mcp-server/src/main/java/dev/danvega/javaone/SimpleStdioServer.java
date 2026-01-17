package dev.danvega.javaone;

import com.fasterxml.jackson.databind.ObjectMapper;  

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures; 
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.LoggingLevel;
import io.modelcontextprotocol.spec.McpSchema.LoggingMessageNotification;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
import io.modelcontextprotocol.spec.McpServerTransportProvider;

import java.nio.charset.StandardCharsets; 
import java.util.Base64;
import java.util.List;
import java.util.Map; 

public class SimpleStdioServer{ 
    public static void main(String[] args) {         
    	new SimpleStdioServer().start(); 
    }

	public void start() {  
		var provider = new StdioServerTransportProvider(new ObjectMapper());
		
		McpServer.sync(provider)
		    .serverInfo("Sample", "1.0.0")
			.capabilities(ServerCapabilities.builder()
					.tools(false)
					.build())
				.tools(CALCULATE_HTTP_BASIC_AUTH_HEADER)
				.build();
	}
     
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

