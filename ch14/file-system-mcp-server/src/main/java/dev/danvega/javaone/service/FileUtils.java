package dev.danvega.javaone.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.modelcontextprotocol.spec.McpSchema.BlobResourceContents;
import io.modelcontextprotocol.spec.McpSchema.ResourceContents;
import io.modelcontextprotocol.spec.McpSchema.TextResourceContents;

/**
 * Utility class for file operations.
 */
public final class FileUtils {

    private FileUtils() {
        // Utility class
    }

    /**
     * Get the current working directory as a File.
     */
    public static File current() {
        return new File(System.getProperty("user.dir"));
    }

    /**
     * List all files in the given base path.
     * 
     * @param basePath the base directory path
     * @return list of relative file paths
     * @throws IOException if an I/O error occurs
     */
    public static List<String> listFiles(Path basePath) throws IOException {
        List<String> files = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(basePath)) {
            stream.filter(Files::isRegularFile)
                  .forEach(path -> {
                      String relativePath = basePath.relativize(path).toString().replace("\\", "/");
                      files.add(relativePath);
                  });
        }
        return files;
    }

    /**
     * List all directory URIs in the given base path.
     * 
     * @param basePath the base directory path
     * @return list of directory URIs
     * @throws IOException if an I/O error occurs
     */
    public static List<String> listDirectoryUris(Path basePath) throws IOException {
        List<String> uris = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(basePath)) {
            stream.filter(Files::isRegularFile)
                  .forEach(path -> {
                      String relativePath = basePath.relativize(path).toString().replace("\\", "/");
                      uris.add("file:///" + relativePath);
                  });
        }
        return uris;
    }

    /**
     * Read file content from the given URI.
     * 
     * @param basePath the base directory path
     * @param uri the file URI (e.g., "file:///path/to/file.txt")
     * @return ResourceContents containing the file content
     * @throws IOException if an I/O error occurs
     */
    public static ResourceContents readFileContent(Path basePath, String uri) throws IOException {
        // Remove the "file:///" prefix to get the relative path
        String relativePath = uri.replaceFirst("file:///", "");
        Path filePath = basePath.resolve(relativePath);
        
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }
        
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        
        // Check if it's a text file
        if (isTextFile(mimeType)) {
            String content = Files.readString(filePath);
            return new TextResourceContents(uri, mimeType, content);
        } else {
            // Return as blob for binary files
            byte[] bytes = Files.readAllBytes(filePath);
            String base64Content = java.util.Base64.getEncoder().encodeToString(bytes);
            return new BlobResourceContents(uri, mimeType, base64Content);
        }
    }

    private static boolean isTextFile(String mimeType) {
        if (mimeType == null) {
            return false;
        }
        return mimeType.startsWith("text/") 
            || mimeType.equals("application/json")
            || mimeType.equals("application/xml")
            || mimeType.equals("application/javascript")
            || mimeType.equals("application/x-yaml");
    }
}
