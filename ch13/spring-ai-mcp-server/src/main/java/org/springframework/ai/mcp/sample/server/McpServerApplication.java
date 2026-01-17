package org.springframework.ai.mcp.sample.server;

import java.util.List;

import org.springframework.ai.mcp.sample.server.service.HttpBasicAuthHeaderService;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
 
@SpringBootApplication
public class McpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpServerApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider calculatorTools(HttpBasicAuthHeaderService service) { 
		return MethodToolCallbackProvider.builder()
				.toolObjects(service)
				.build();
	}
	public record HttpBasicAuthHeader(String username , String password) {
	}
	public record TextInput(String input) {
	}
	@Bean
	public ToolCallback toUpperCase() {
		return FunctionToolCallback.builder("toUpperCase", (TextInput input) -> input.input().toUpperCase())
			.inputType(TextInput.class)
			.description("Put the text to upper case")
			.build();
	}

}