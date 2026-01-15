# PDF Q&A

[![build](https://github.com/JavaAIDev/pdf-qa/actions/workflows/build.yaml/badge.svg)](https://github.com/JavaAIDev/pdf-qa/actions/workflows/build.yaml)

> See JavaAIDev [article](https://javaaidev.com/docs/rag/samples/pdf-qa) for more details.

Q&A based on content of PDF files.

|模型名稱	         |維度	|說明   |
|-------------------|------|-------|
|nomic-embed-text	|768   |最常用，效果好|
|bge-large　　　　　　|1024  |高品質|
|mxbai-embed-large	|1024  |高品質|
|all-minilm     	|384   |輕量快速|
|bge-m3	            |1024  |多語言支援|

# vector_store Clean
```bash
-- 連接到 PostgreSQL 執行
docker exec -it  pdf-qa-postgres-1 bash
psql -U postgres -d postgres

-- 連接到 PostgreSQL 執行
TRUNCATE TABLE vector_store;
-- 或者刪除並讓應用重建
DROP TABLE IF EXISTS vector_store;
```