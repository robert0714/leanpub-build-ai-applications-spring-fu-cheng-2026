package dev.danvega.javaone;


import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.LoggingLevel;
import io.modelcontextprotocol.spec.McpSchema.LoggingMessageNotification;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;

import java.nio.charset.StandardCharsets; 
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

public class SimpleHttpSseServer{ 
	private static final Logger log = LoggerFactory.getLogger(SimpleHttpSseServer.class);
    public static void main(String[] args) {         
    	new SimpleHttpSseServer().start(); 
    }

	public void start() {  
		log.info("Starting MCP Server...");
		
	   /**
         * Note, HttpServletSseServerTransportProvider extends HttpServlet
         */
		HttpServletSseServerTransportProvider provider = HttpServletSseServerTransportProvider.builder()
				      .messageEndpoint("/messages")
				     .build();
		
		// Create a server with custom configuration
		McpSyncServer syncServer = McpServer.sync(provider)
		    .serverInfo("Sample", "1.0.0")
		    .capabilities(ServerCapabilities.builder()
		       .logging()
		       .tools(false)
		       .build())
		    .tools(CALCULATE_HTTP_BASIC_AUTH_HEADER)
		    .build();

		
		log.info("MCP Server info: {}", syncServer.getServerInfo());
		 
		// Set up Jetty with a context handler
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		contextHandler.setContextPath("/");

		// Add the MCP transport provider as a servlet
		ServletHolder servletHolder = new ServletHolder(provider);
		contextHandler.addServlet(servletHolder, "/*");
		
		// Start Jetty on port 9000
        Server server = new Server(9000);
        server.setHandler(contextHandler);
        
        try {
            server.start();
            log.info("Jetty server started on port 8080");
            
            // Add a shutdown hook for clean shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    log.info("Shutting down MCP server...");
                    syncServer.close();
                    server.stop();
                } catch (Exception e) {
                    log.error("Error during shutdown", e);
                }
            }));
            
            server.join(); // Wait for the server to exit
        } catch (Exception e) {
            log.error("Error starting server", e);
            syncServer.close();
            throw new RuntimeException(e);
        }
	}
     
	public static final McpServerFeatures.SyncToolSpecification 
	CALCULATE_HTTP_BASIC_AUTH_HEADER 
	 = new McpServerFeatures.SyncToolSpecification(
			new McpSchema.Tool(
					"calculateHttpBasicAuthHeader",
					"Calculate value of HTTP Authorization header for basic auth",
					new McpSchema.JsonSchema(
							"object",
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

