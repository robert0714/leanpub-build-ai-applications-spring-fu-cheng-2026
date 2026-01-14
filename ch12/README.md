# Chapter 12 RAG Examples

## Text-to-SQL
This section describes a Text-to-SQL implementation using Spring AI. Given a user query, the actual query results will be returned by providing a tool for LLM to execute SQL statements. The complete source is available on GitHub [JavaAIDev/simple-text-to-sql](https://github.com/JavaAIDev/simple-text-to-sql).

### Prerequisites
Before running the Text-to-SQL application, you should have:

* Java 17
* A running Postgres server with sample table data loaded. https://github.com/neondatabase/postgres-sample-dbs?tab=readme-ov-file#netflix-data