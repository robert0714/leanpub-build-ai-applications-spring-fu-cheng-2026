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


The final step is to test the MCP server. A separate tool [MCP Inspector](https://github.com/modelcontextprotocol/inspector) is used here.

```bash
docker run --rm \
  -p 127.0.0.1:6274:6274 \
  -p 127.0.0.1:6277:6277 \
  -e HOST=0.0.0.0 \
  -e MCP_AUTO_OPEN_ENABLED=false \
  ghcr.io/modelcontextprotocol/inspector:latest
```
The inspector runs both an MCP Inspector (MCPI) client UI (default port 6274) and an MCP Proxy (MCPP) server (default port 6277). Open the MCPI client UI in your browser to use the inspector. (These ports are derived from the T9 dialpad mapping of MCPI and MCPP respectively, as a mnemonic). 

## HTTP SSE Server

