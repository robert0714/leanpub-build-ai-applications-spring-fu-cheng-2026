package com.javaaidev.mcp.server;


import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class SimpleStdioClient{ 
	private static final Logger log = LoggerFactory.getLogger(SimpleStdioClient.class);

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("JAR file is required");
			return;
		}
		var client = new SimpleStdioClient();
		client.connect(args[0]);
	}
     
	public void connect(String jarFilePath) {
		var transport = new StdioClientTransport(
				ServerParameters.builder("java")
				.args("-jar", jarFilePath)
				.build());
		
		try (var client = McpClient.sync(transport).build()) {
			var initializeResult = client.initialize();
			log.info("Client initialized: {}", initializeResult);
			var callToolResult = client.callTool(
					new CallToolRequest("calculateHttpBasicAuthHeader",
					Map.of(
							"username", "admin", 
							"password", "password"
							)));
			System.out.println("----------------------------------");
			log.info("Tool call callToolResult: {}", callToolResult);
		}
	}
}

