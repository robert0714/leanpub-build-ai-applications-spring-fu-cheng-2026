package dev.danvega.javaone;


import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport; 
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest; 
import java.util.Map;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient; 

public class SpringHttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(
       SpringHttpClient.class);
	 
	  public void connect(String baseUrl) {
	    var transport = WebFluxSseClientTransport.builder(
	        WebClient.builder().baseUrl(baseUrl)).build();
	    try (var client = McpClient.sync(transport).build()) {
	      var initializeResult = client.initialize();
	      LOGGER.info("Client initialized: {}", initializeResult);
	      var callToolResult = client.callTool(
	          new CallToolRequest("calculateHttpBasicAuthHeader", Map.of(
	              "username", "admin",
	              "password", "password"
	          )));
	      LOGGER.info("Tool call callToolResult: {}", callToolResult);
	    }
	  }

	  public static void main(String[] args) {
	    if (args.length < 1) {
	      System.out.println("Base url is required");
	      return;
	    }
	    var client = new SpringHttpClient();
	    client.connect(args[0]);
	  }
	}