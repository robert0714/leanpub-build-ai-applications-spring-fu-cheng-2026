package dev.danvega.hub.conf;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import dev.danvega.hub.service.VectorStoreService;

@Configuration
public class VectorStoreConfiguration { 
	
	/**
	 * 使用 PgVectorStore 作為向量儲存
	 * Spring AI 會自動配置 PgVectorStore Bean（透過 spring-ai-starter-vector-store-pgvector）
	 * 這裡注入通用的 VectorStore 介面
	 */
	@Bean
	public VectorStoreService vectorStoreService(VectorStore vectorStore) {
		return new VectorStoreService(vectorStore);
	}
}