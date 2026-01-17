package dev.danvega.javaone;
 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.WebFluxSseServerTransportProvider;
import io.modelcontextprotocol.server.transport.WebMvcSseServerTransportProvider; 
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;

@Configuration
public class AppConfiguration {
    @Bean
    public RouterFunction<ServerResponse> mcpServerRouterFunction() {
      var provider = new WebMvcSseServerTransportProvider(
          new ObjectMapper(), "/message");
      McpServer.sync(provider)
          .serverInfo("spring-webmvc-server", "1.0.0")
          .capabilities(ServerCapabilities.builder()
            .tools(false)
            .build())
          .tools( Tools.CALCULATE_HTTP_BASIC_AUTH_HEADER)
          .build();
      return provider.getRouterFunction();
    }
//      @Bean
//      public RouterFunction<?> mcpServerRouterFunction() {
//        var provider = WebFluxSseServerTransportProvider.builder()
//            .objectMapper(new ObjectMapper())
//            .messageEndpoint("/message")
//            .build();
//        McpServer.sync(provider)
//            .serverInfo("spring-webflux-server", "1.0.0")
//             .capabilities(ServerCapabilities.builder()
//                 .tools(false)
//                 .build())
//             .tools(Tools.CALCULATE_HTTP_BASIC_AUTH_HEADER)
//             .build();
//         return provider.getRouterFunction();
//       }
}
