package dev.danvega.javaone;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class SpringApplication { 
	private static final Logger log = LoggerFactory.getLogger(SpringApplication.class);
	
	public static void main(String[] args) {		
		// Set up Jetty with a context handler
		ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		
		contextHandler.setContextPath("/");
		
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.register(AppConfiguration.class);
		contextHandler.addEventListener(new ContextLoaderListener(applicationContext));
		
		// Start Jetty on port 9000
        final Server server = new Server(9000);
        server.setHandler(contextHandler);	   
        
		contextHandler.addServlet(new ServletHolder(new DispatcherServlet(applicationContext)), "/*");
		
        try {
        	log.info("Starting MCP Server...");
            server.start();
            server.join();            
            log.info("Jetty server started on port 9000");
            // Add a shutdown hook for clean shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    log.info("Shutting down MCP server..."); 
                    server.stop();
                } catch (Exception e) {
                    log.error("Error during shutdown", e);
                }
            }));            
            server.join(); // Wait for the server to exit
        } catch (Exception e) {
            log.error("Error starting server", e); 
            throw new RuntimeException(e);
        } 
	}
}
