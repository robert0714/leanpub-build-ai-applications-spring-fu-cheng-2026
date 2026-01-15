# Chapter 12 RAG Examples

## Text-to-SQL
> [!NOTE] 
This section describes a Text-to-SQL implementation using Spring AI. Given a user query, the actual query results will be returned by providing a tool for LLM to execute SQL statements. The complete source is available on GitHub [JavaAIDev/simple-text-to-sql](https://github.com/JavaAIDev/simple-text-to-sql).

### Prerequisites
Before running the Text-to-SQL application, you should have:

* Java 17
* A running Postgres server with sample table data loaded. https://github.com/neondatabase/postgres-sample-dbs?tab=readme-ov-file#netflix-data

## PDF Q&A
> [!NOTE] 
This section describes a PDF Q&A implementation using Spring AI. Given a PDF file, this sample application loads its content into a vector store, then uses LLM to answer user’s query based on the content. The complete source is available on GitHub [JavaAIDev/pdf-qa](https://github.com/JavaAIDev/pdf-qa).

PDF Q&A is a classical example of using RAG. Content of PDF files are used to provide context for an LLM to answer queries.

Start the server and use Swagger UI to test the API.
* http://localhost:8063/webjars/chat-agent-ui/index.html
* http://localhost:8063/swagger-ui/index.html