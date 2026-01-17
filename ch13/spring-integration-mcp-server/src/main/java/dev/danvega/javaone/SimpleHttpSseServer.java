package dev.danvega.javaone;


import io.modelcontextprotocol.server.McpServer; 
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.HttpServletSseServerTransportProvider; 
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;
 

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
		    .tools(Tools.CALCULATE_HTTP_BASIC_AUTH_HEADER)
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
}

