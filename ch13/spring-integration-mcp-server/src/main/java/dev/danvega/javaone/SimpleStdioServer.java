package dev.danvega.javaone;

import com.fasterxml.jackson.databind.ObjectMapper;  

import io.modelcontextprotocol.server.McpServer; 
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider; 
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;  

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
				.tools(Tools.CALCULATE_HTTP_BASIC_AUTH_HEADER)
				.build();
	} 
}

