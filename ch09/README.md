# Chapter 09 Vector Store
* https://docs.spring.io/spring-ai/reference/api/vectordbs.html

## Pgvector
Spring AI built-in SimpleVectorStore should only be used in creating proof-of-concept applications. To create production-ready applications, we need to choose other vector stores.

[pgvector](https://github.com/pgvector/pgvector) is used as a sample vector store.

### Installation
pgvector is a Postgres extension, so it can be installed into existing Postgres databases, see [installation guide](https://github.com/pgvector/pgvector#installation).