# Chapter 13 MCP Quick Start
## MCP Introduction
MCP (Model Context Protocol) defines two roles: server and client.The server and the client interact with each other using a standard protocol. MCP server is responsible for providing knowledge, including prompt templates, resources, and tools. MCP client interacts with the server to obtain these knowledge, including getting prompts, the content of resources, and invoking tools. A JSON-RPC based protocol is used between the server and the client.

* Official Document: https://modelcontextprotocol.io/sdk/java/mcp-server
* Microsoft Document:  https://github.com/microsoft/mcp-for-beginners
## Java Development Basics
In order to add the MCP SDK dependencies to the project, we can import the mcp-bom dependency first.
[See mcp-bom dependency](https://github.com/JavaAIDev/easy-mcp-client/blob/main/pom.xml#L73-L79)

The MCP SDK contains several modules, and in most cases we will only need to use the [mcp module](https://github.com/JavaAIDev/easy-mcp-client/blob/main/pom.xml#L30-L33). This module already contains the MCP server and client implementations, as well as the basic implementation of stdio and HTTP transports.
## stdio server
* Spring-ai document: https://docs.spring.io/spring-ai/reference/api/mcp/mcp-stdio-sse-server-boot-starter-docs.html
* Official Document: 
  * https://modelcontextprotocol.io/docs/develop/build-server#java
  * https://modelcontextprotocol.io/sdk/java/mcp-server#server-transport-providers
  * https://modelcontextprotocol.io/sdk/java/mcp-server#sync-api-2
 
## Running the MCP Inspector
### Requirements
* Node.js: ^22.7.5
### Quick Start (UI mode)
To get up and running right away with the UI, just execute the following:
```bash
npx @modelcontextprotocol/inspector
```
The server will start up and the UI will be accessible at http://localhost:6274.
### Docker Container
You can also start it in a Docker container with the following command:
```bash
docker run --rm \
  -p 127.0.0.1:6274:6274 \
  -p 127.0.0.1:6277:6277 \
  -e HOST=0.0.0.0 \
  -e MCP_AUTO_OPEN_ENABLED=false \
  ghcr.io/modelcontextprotocol/inspector:latest
```
In MCP Inspector web UI:
* Transport Type: `STIDO`
* Command: `java` 
* Arguments: `-jar D:/Data/workspaces/STS-5.0.x/leanpub-build-ai-applications-spring-fu-cheng-2026/ch13/simple-mcp-server/target/simple-mcp-server-0.0.1-SNAPSHOT.jar`
* Next Step: cleck `Connect`

The inspector runs both an MCP Inspector (MCPI) client UI (default port 6274) and an MCP Proxy (MCPP) server (default port 6277). Open the MCPI client UI in your browser to use the inspector. (These ports are derived from the T9 dialpad mapping of MCPI and MCPP respectively, as a mnemonic). 
## Running  Claude Desktop
* Reference: https://modelcontextprotocol.io/docs/develop/connect-local-servers
* Configuration
  * Location: 
    * macOS/Linux: `~/Library/Application Support/Claude/claude_desktop_config.json`
    * Windows: `%APPDATA%\Claude\claude_desktop_config.json`  or `%USERPROFILE%\AppData\Roaming\Claude\logs\claude_desktop_config.json`
  * Content: 
    ```json
    {
      "mcpServers": {
        "my-custom-server": {
          "type": "stdio",
          "command": "java",
          "args": [
            "-jar",  
            "D:/Data/workspaces/STS-5.0.x/leanpub-build-ai-applications-spring-fu-cheng-2026/ch13/simple-mcp-server/target/simple-mcp-server-0.0.1-SNAPSHOT.jar"
          ]
        }
      }
    }
    ```
* Logs
  Claude Desktop's log help you discover th Errors：
  * macOS: `~/Library/Logs/Claude/mcp.log`
  * Windows: `%APPDATA%\Claude\logs\mcp.log`  or `%USERPROFILE%\AppData\Roaming\Claude\logs\mcp.log`  
 
## Running GitHub Copilot in Visual Studio Code (VS Code)
* Reference: 
  * https://vscode.com.tw/docs/copilot/customization/mcp-servers
  * https://code.visualstudio.com/docs/copilot/customization/mcp-servers
* Add an MCP server to a workspace `mcp.json` file
  * Location: Create a `.vscode/mcp.json` file in your workspace.
  * Content: 
    ```json
    {
      "servers": {
        "my-custom-server": {
          "command": "java",
          "args": [
            "-jar",  
            "D:/Data/workspaces/STS-5.0.x/leanpub-build-ai-applications-spring-fu-cheng-2026/ch13/simple-mcp-server/target/simple-mcp-server-0.0.1-SNAPSHOT.jar"
          ]
        }
      }
    }
    ```
* Usage: 
  * https://vscode.com.tw/docs/copilot/customization/mcp-servers#_use-mcp-tools-in-chat
  * Steps:
    * Startup mcp server  
    * call
      * Switch Agent mode
      * In console , to type `#my-custom-server` `#calculateHttpBasicAuthHeader` 
        * my-custom-server: in `mcp.json` 
        * calculateHttpBasicAuthHeader: wh you typed `#my-custom-server` and cliced `Enter` .
* Logs
  * https://vscode.com.tw/docs/copilot/customization/mcp-servers#_troubleshoot-and-debug-mcp-servers


## Running Google Antigravity
* Reference: 
  * https://codelabs.developers.google.com/getting-started-google-antigravity?hl=zh-tw#1
  * https://antigravity.google/docs/mcp
  * https://firebase.google.com/docs/ai-assistance/mcp-server?hl=zh-tw
  * https://cloud.google.com/blog/products/data-analytics/connect-google-antigravity-ide-to-googles-data-cloud-services
* Add an MCP server to a workspace `mcp.json` file
  * Location: 
    * macOS/Linux: `~/.gemini/antigravity/mcp_config.json`
    * Windows: `%USERPROFILE%\.gemini\antigravity\mcp_config.json`
  * Content: 
    ```json
    {
      "mcpServers": {
        "my-custom-server": {
          "command": "java",
          "args": [
            "-jar",  
            "D:/Data/workspaces/STS-5.0.x/leanpub-build-ai-applications-spring-fu-cheng-2026/ch13/simple-mcp-server/target/simple-mcp-server-0.0.1-SNAPSHOT.jar"
          ]
        }
      }
    }
    ```
## HTTP SSE Server
* references
  * https://github.com/stantonk/mcp-server-java-sse-http-demo
  * https://www.youtube.com/watch?v=Y_Rk6QgWUbE
  * https://github.com/danvega/javaone-mcp


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
