package com.javaaidev.mcp.server;


import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
 
import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class SimpleHttpClient{ 
	private static final Logger log = LoggerFactory.getLogger(SimpleHttpClient.class);
    

    public void connect(String baseUri) {
         var transport = HttpClientSseClientTransport.builder(baseUri)
            .build();
       try (var client = McpClient.sync(transport).build()) { 
           var initializeResult = client.initialize();
           log.info("Client initialized: {}", initializeResult);
           var callToolResult = client.callTool(
                new CallToolRequest("calculateHttpBasicAuthHeader", Map.of(
                 "username", "admin",
                 "password", "password"
           )));
           
       System.out.println("----------------------------------");    
       log.info("Tool call callToolResult: {}", callToolResult);
     }
   }

   public static void main(String[] args) {
     if (args.length < 1) {
       System.out.println("Base url is required");
       return;
     }
     var client = new SimpleHttpClient();
     client.connect(args[0]);
   }
 }