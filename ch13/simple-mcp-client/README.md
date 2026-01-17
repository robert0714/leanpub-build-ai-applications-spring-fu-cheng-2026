* https://www.youtube.com/watch?v=Y_Rk6QgWUbE
* https://github.com/danvega/javaone-mcp
* https://github.com/stantonk/mcp-server-java-sse-http-demo

# MCP Client
* https://modelcontextprotocol.io/docs/develop/build-client#java


```bash
java -jar ./target/simple-mcp-client-0.0.1-SNAPSHOT.jar D:\Data\workspaces\STS-5.0.x\leanpub-build-ai-applications-spring-fu-cheng-2026\ch13\simple-mcp-server\target\simple-mcp-server-0.0.1-SNAPSHOT.jar

[pool-4-thread-1] INFO io.modelcontextprotocol.client.transport.StdioClientTransport - STDERR Message received: [pool-1-thread-1] INFO io.modelcontextprotocol.server.McpAsyncServer - Client initialize request - Protocol: 2024-11-05, Capabilities: ClientCapabilities[experimental=null, roots=null, sampling=null], Info: Implementation[name=Java SDK MCP Client, version=1.0.0]      
[pool-1-thread-1] INFO io.modelcontextprotocol.client.McpAsyncClient - Server response with Protocol: 2024-11-05, Capabilities: ServerCapabilities[completions=null, experimental=null, logging=LoggingCapabilities[], prompts=null, resources=null, tools=ToolCapabilities[listChanged=false]], Info: Implementation[name=Sample, version=1.0.0] and Instructions null
[main] INFO dev.danvega.javaone.SimpleStdioClient - Client initialized: InitializeResult[protocolVersion=2024-11-05, capabilities=ServerCapabilities[completions=null, experimental=null, logging=LoggingCapabilities[], prompts=null, resources=null, tools=ToolCapabilities[listChanged=false]], serverInfo=Implementation[name=Sample, version=1.0.0], instructions=null]
----------------------------------
[main] INFO dev.danvega.javaone.SimpleStdioClient - Tool call callToolResult: CallToolResult[content=[TextContent[audience=null, priority=null, text=Basic YWRtaW46cGFzc3dvcmQ=]], isError=false]
[parallel-4] WARN io.modelcontextprotocol.client.transport.StdioClientTransport - Process terminated with code 1
```
