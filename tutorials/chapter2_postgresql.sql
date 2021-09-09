CREATE TABLE unary (a int);

INSERT INTO unary(a)
SELECT i
FROM generate_series(1,100,1) AS i;

EXPLAIN VERBOSE
SELECT u.*
FROM unary AS u;

show data_directory;

SELECT u.ctid, u.*
FROM unary AS u;



SELECT pg_typeof(a) from unary;

ALTER TABLE unary
ALTER COLUMN a SET DATA TYPE text;
