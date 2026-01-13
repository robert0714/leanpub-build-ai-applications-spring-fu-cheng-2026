package dev.danvega.hub.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;

public class DataGenerator {
	private final SimpleVectorStore simpleVectorStore;
	public static final Path SAVE_PATH = Paths.get(".", "saved_docs.json");

	public DataGenerator(SimpleVectorStore simpleVectorStore) {
		this.simpleVectorStore = simpleVectorStore;
//		Files.createTempDirectory("myapp-", ".tmp");
	}

	public void generate() {
		var list = List.of("Alex", "Bob", "David");
		var docs = list.stream().map(name -> new Document("Hello " + name, Map.of("name", name))).toList();
		simpleVectorStore.add(docs);
		simpleVectorStore.save(SAVE_PATH.toFile());
	}

}
