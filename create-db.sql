SELECT 'CREATE DATABASE trade_project'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'trade_project');