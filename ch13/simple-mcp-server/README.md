* https://www.youtube.com/watch?v=Y_Rk6QgWUbE
* https://github.com/danvega/javaone-mcp
* https://github.com/stantonk/mcp-server-java-sse-http-demo

# MCP Inspector
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

## Running Claude Desktop
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
  * 
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
 