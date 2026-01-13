package dev.danvega.hub.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter.Expression;
import org.springframework.ai.vectorstore.filter.Filter.ExpressionType;
import org.springframework.ai.vectorstore.filter.Filter.Key;
import org.springframework.ai.vectorstore.filter.Filter.Value;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.util.CollectionUtils;
 

public class VectorStoreService {
	private static final String METADATA_DOCUMENT_ID = "__id__";
	private final VectorStore vectorStore;

	public VectorStoreService(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}

	public String add(Document document) {
		document.getMetadata().put(METADATA_DOCUMENT_ID, document.getId());
		vectorStore.add(List.of(document));
		return document.getId();
	}

	public void delete(String documentId) {
		vectorStore.delete(List.of(documentId));
	}

	public Optional<Document> getById(String documentId) {
	    // 使用 metadata 過濾查詢特定 ID 的文檔
	    SearchRequest request = SearchRequest.builder()
	        .query("") // 空查詢
	        .topK(1)
	        .filterExpression(new FilterExpressionBuilder().eq("id", documentId).build())
	        .build();
	    
	    List<Document> results = vectorStore.similaritySearch(request);
	    return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	  }

	public List<Document> query(String query, Map<String, String> metadataFilter) {
		var requestBuilder = SearchRequest.builder().query(query).topK(5);
		if (!CollectionUtils.isEmpty(metadataFilter)) {
			var filters = metadataFilter.entrySet().stream().map(
					entry -> new Expression(ExpressionType.EQ, new Key(entry.getKey()), new Value(entry.getValue())))
					.toList();
			if (filters.size() == 1) {
				requestBuilder.filterExpression(filters.get(0));
			} else {
				var expr = new Expression(ExpressionType.AND, filters.get(0), filters.get(1));
				for (Expression expression : filters.subList(2, filters.size())) {
					expr = new Expression(ExpressionType.AND, expr, expression);
				}
				requestBuilder.filterExpression(expr);
			}
		}
		return vectorStore.similaritySearch(requestBuilder.build());
	}
	/**
	 * DocumentQuery migration to SearchRequest spring-ai <p>
	 * https://docs.spring.io/spring-ai/reference/api/vectordbs.html <p>
	 * https://spring.io/blog/2025/05/23/vector-search-methods <p>
	 * https://docs.spring.io/spring-ai/docs/current/api/org/springframework/ai/vectorstore/SearchRequest.html <p>
	 * 
	 * **/
	public List<Document> query(String query, String metadataFilter) {
	    SearchRequest.Builder requestBuilder = SearchRequest.builder()
	        .query(query)
	        .topK(5); // 預設返回 5 個結果
	    
	    // 如果有 metadata 過濾條件,添加過濾表達式
	    if (metadataFilter != null && !metadataFilter.isEmpty()) {
	      // 根據你的需求解析 metadataFilter 並構建過濾表達式
	      // 例如: "category == 'tech'"
	      requestBuilder.filterExpression(
	          new FilterExpressionBuilder().eq("category", metadataFilter).build()
	      );
	    }	    
	    return vectorStore.similaritySearch(requestBuilder.build());
	  }
}
