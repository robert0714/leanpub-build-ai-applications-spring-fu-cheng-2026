package dev.danvega.javaone.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.modelcontextprotocol.server.McpServerFeatures.SyncCompletionSpecification;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult;
import io.modelcontextprotocol.spec.McpSchema.CompleteResult.CompleteCompletion;
import io.modelcontextprotocol.spec.McpSchema.ResourceReference;

public class FileResourceCompletions {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileResourceCompletions.class);

	private Path basePath;

	public FileResourceCompletions(Path basePath) {
		this.basePath = basePath;
	}

	public void setBasePath(Path basePath) {
		this.basePath = basePath;
	}

	public SyncCompletionSpecification completion() {
		return new SyncCompletionSpecification(new ResourceReference("file:///{filename}"), (exchange, request) -> {
			try {
				var files = FileUtils.listFiles(basePath).stream()
						.filter(path -> path.toLowerCase().contains(request.argument().value().toLowerCase())).toList();
				return new CompleteResult(new CompleteCompletion(files, files.size(), false));
			} catch (IOException e) {
				LOGGER.error("Failed to get completions", e);
				return new CompleteResult(new CompleteCompletion(List.of(), 0, false));
			}
		});
	}
}