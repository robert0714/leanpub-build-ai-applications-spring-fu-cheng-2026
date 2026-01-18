package dev.danvega.javaone.service;


import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.modelcontextprotocol.server.McpServerFeatures.SyncResourceSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceRequest;
import io.modelcontextprotocol.spec.McpSchema.ReadResourceResult;

public class FileResources {
    private static final Logger LOGGER = LoggerFactory.getLogger(
        FileResources.class);
    private final List<String> resourceUris = new ArrayList<>();
 
    private McpSyncServer mcpSyncServer;
    private Path basePath;
 
  public FileResources(Path basePath) {
    this.basePath = basePath;
  }
 
  public void setMcpSyncServer(McpSyncServer mcpSyncServer) {
    this.mcpSyncServer = mcpSyncServer;
    init();
  }
 
  public void setBasePath(Path basePath) {
    this.basePath = basePath;
    init();
  }
 
  private void init() {
    resourceUris.forEach(mcpSyncServer::removeResource);
    resourceUris.clear();
    try {
      FileUtils.listDirectoryUris(basePath).forEach(uri -> {
        mcpSyncServer.addResource(new SyncResourceSpecification(
             new McpSchema.Resource(uri, "files in directory " + uri, 
                 "File resource", "application/octet-stream", null),
             ((exchange, request) -> handleReadResource(request))));
        resourceUris.add(uri);
      });
    } catch (Exception e) {
      LOGGER.error("Failed to init path", e);
    }
  }

  private ReadResourceResult handleReadResource(ReadResourceRequest request) {
    var uri = request.uri();
    try {
      return new ReadResourceResult(
          List.of(FileUtils.readFileContent(basePath, uri)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}