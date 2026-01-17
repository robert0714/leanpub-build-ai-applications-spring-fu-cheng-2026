package org.springframework.ai.mcp.sample.server.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
 

/**
 * Service for HTTP Basic Authentication header generation.
 * This service provides functionality to generate HTTP Authorization headers
 * for Basic Authentication through MCP.
 */
@Service
public class HttpBasicAuthHeaderService {

    /**
     * Calculate value of HTTP Authorization header for basic auth.
     * 
     * @param username The username for authentication
     * @param password The password for authentication
     * @return The HTTP Authorization header value in format "Basic {base64-encoded-credentials}"
     */
    @Tool(description = "Calculate value of HTTP Authorization header for basic auth")
    public String calculateHttpBasicAuthHeader(
    		@ToolParam(description = "The username for authentication", required = true)String username, 
    		@ToolParam(description = "The password for authentication", required = false)String password) {
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encodedCredentials;
    }
}
