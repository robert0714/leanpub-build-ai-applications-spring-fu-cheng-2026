package dev.danvega.javaone.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;

public class FileTools {

    private Path basePath;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FileTools(Path basePath) {
        this.basePath = basePath;
    }

    public void setBasePath(Path basePath) {
        this.basePath = basePath;
    }

    public McpServerFeatures.SyncToolSpecification[] tools() {
        return new McpServerFeatures.SyncToolSpecification[] {
            new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(
                    "readFile",
                    "Read file content",
                    new McpSchema.JsonSchema("object", 
                        Map.of("path", new McpSchema.JsonSchema("string", Map.of(), List.of(), false, Map.of(), Map.of())),
                        List.of("path"), false, Map.of(), Map.of())),
                (exchange, request) -> readFileContent((String) request.get("path"))),
            new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(
                    "listFiles",
                    "List all files in the base directory",
                    new McpSchema.JsonSchema("object", Map.of(), List.of(), false, Map.of(), Map.of())),
                (exchange, request) -> listFiles()),
            new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(
                    "writeFile",
                    "Write file content",
                    new McpSchema.JsonSchema("object", 
                        Map.of("path", new McpSchema.JsonSchema("string", Map.of(), List.of(), false, Map.of(), Map.of()),
                               "content", new McpSchema.JsonSchema("string", Map.of(), List.of(), false, Map.of(), Map.of())),
                        List.of("path", "content"), false, Map.of(), Map.of())),
                (exchange, request) -> writeFile(
                    (String) request.get("path"),
                    (String) request.get("content"))),
            new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(
                    "deleteFile",
                    "Delete file",
                    new McpSchema.JsonSchema("object", 
                        Map.of("path", new McpSchema.JsonSchema("string", Map.of(), List.of(), false, Map.of(), Map.of())),
                        List.of("path"), false, Map.of(), Map.of())),
                (exchange, request) -> deleteFile((String) request.get("path")))
        };
    }

    public CallToolResult readFileContent(String path) {
        try {
            var resourceContent = FileUtils.readFileContent(basePath, "file:///" + StringUtils.removeStart(path, "/"));
            // Return text content for the file
            String content;
            if (resourceContent instanceof McpSchema.TextResourceContents textContent) {
                content = textContent.text();
            } else if (resourceContent instanceof McpSchema.BlobResourceContents blobContent) {
                content = "[Binary file: " + blobContent.mimeType() + "]";
            } else {
                content = resourceContent.toString();
            }
            return new CallToolResult(List.of(new TextContent(content)), false);
        } catch (IOException e) {
            return new CallToolResult(List.of(new TextContent("Failed to read file content: " + e.getMessage())), true);
        }
    }

    public CallToolResult listFiles() {
        try {
            var files = FileUtils.listFiles(basePath);
            return new CallToolResult(toJson(files), false);
        } catch (Exception e) {
            return new CallToolResult(e.getMessage(), true);
        }
    }

    public CallToolResult writeFile(String path, String content) {
        var filePath = basePath.resolve(StringUtils.removeStart(path, "/"));
        try {
            Files.writeString(filePath, content, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.CREATE);
            return new CallToolResult(List.of(new TextContent("wrote to file " + path)), false);
        } catch (IOException e) {
            return new CallToolResult(List.of(new TextContent(e.getMessage())), true);
        }
    }

    public CallToolResult deleteFile(String path) {
        var filePath = basePath.resolve(StringUtils.removeStart(path, "/"));
        try {
            Files.delete(filePath);
            return new CallToolResult(List.of(new TextContent(path + " deleted")), false);
        } catch (IOException e) {
            return new CallToolResult(List.of(new TextContent(e.getMessage())), true);
        }
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}