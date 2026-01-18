package dev.danvega.javaone;

import dev.danvega.javaone.service.FileChangedNotifier;
import dev.danvega.javaone.service.FileResourceCompletions;
import dev.danvega.javaone.service.FileResources;
import dev.danvega.javaone.service.FileTools;
import dev.danvega.javaone.service.FileUtils;
import dev.danvega.javaone.service.LocalFilesWatcher;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;

public class FileSystemServer implements FileChangedNotifier {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        FileSystemServer.class);
 
   private Path basePath;
   private final McpSyncServer mcpSyncServer;
   private final FileTools fileTools;
   private final FileResources fileResources;
   private final FileResourceCompletions fileResourceCompletions;

   private final LocalFilesWatcher fileWatcher = new LocalFilesWatcher(this);
   public FileSystemServer(Path basePath) {
     LOGGER.info("Start file system server for path: {}", basePath);
     this.basePath = basePath;
     fileTools = new FileTools(basePath);
     fileResources = new FileResources(basePath);
     fileResourceCompletions = new FileResourceCompletions(basePath);
     watchFileChanges();
     mcpSyncServer = McpServer.sync(new StdioServerTransportProvider())
         .serverInfo("file-system", "1.0.0")
         .capabilities(ServerCapabilities.builder()
             .resources(true, true)
             .tools(true)
             .completions()
             .logging()
             .build())
         .tools(fileTools.tools())
         .completions(fileResourceCompletions.completion())
         .rootsChangeHandler(((exchange, roots) -> {
           if (roots == null || roots.isEmpty()) {
             return;
           }
           var root = roots.get(0).uri();
           var rootPath = Path.of(URI.create(root));
           LOGGER.info("base path set to {}", rootPath);
           this.basePath = rootPath;
           fileTools.setBasePath(rootPath);
           fileResources.setBasePath(rootPath);
           fileResourceCompletions.setBasePath(rootPath);
           this.watchFileChanges();
         }))
         .build();
     fileResources.setMcpSyncServer(mcpSyncServer);
   }
 
   private void watchFileChanges() {
     try {
       fileWatcher.watch(basePath);
     } catch (IOException e) {
       LOGGER.error("Failed to watch file changes", e);
     }
   }
 
   @Override
  public void notifyFileCreated(File file) {
    mcpSyncServer.notifyResourcesListChanged();
  }

  @Override
  public void notifyFileChanged(File file) {
    mcpSyncServer.notifyResourcesListChanged();
  }

  @Override
  public void notifyFileDeleted(File file) {
    mcpSyncServer.notifyResourcesListChanged();
  }
 
  public static void main(String[] args) {
    Path basePath;
    if (args.length > 0) {
      basePath = Paths.get(args[0]).normalize();
    } else {
      basePath = FileUtils.current().toPath().toAbsolutePath();
    }
    new FileSystemServer(basePath);
  }
}