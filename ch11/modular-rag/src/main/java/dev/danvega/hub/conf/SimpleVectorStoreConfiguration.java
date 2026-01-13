package dev.danvega.hub.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.danvega.hub.service.DataGenerator;
import dev.danvega.hub.service.VectorStoreService;

@Configuration
public class SimpleVectorStoreConfiguration {

	@Bean
	public SimpleLoggerAdvisor simpleLoggerAdvisor() {
		return new SimpleLoggerAdvisor();
	}

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Bean
	public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
		var vectorStore = SimpleVectorStore.builder(embeddingModel).build();
		var saveFile = DataGenerator.SAVE_PATH.toFile();
		// 只有當文件存在且不為空時才載入
		if (saveFile.exists() && saveFile.length() > 0) {
			try {
				vectorStore.load(saveFile);
				logger.info("Successfully loaded documents from {}", saveFile.getAbsolutePath());
			} catch (Exception e) {
				logger.error("Failed to load documents from {}, skip", saveFile.getAbsolutePath(), e);
			}
		} else {
			logger.info("No saved documents found at {}, starting with empty vector store", saveFile.getAbsolutePath());
		}
		return vectorStore;
	}

	@Bean
	public DataGenerator dataGenerator(SimpleVectorStore simpleVectorStore) {
		return new DataGenerator(simpleVectorStore);
	}
	@Bean
	public VectorStoreService vectorStoreService(SimpleVectorStore simpleVectorStore) {
		return new VectorStoreService(simpleVectorStore);
	}
}