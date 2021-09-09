-- Andreas Schmid (3087156)
-- Tobias Stumpp (3798377)

DROP TABLE IF EXISTS lists CASCADE;

CREATE TABLE lists (
  id integer NOT NULL,
  pos integer NOT NULL,
  val float NOT NULL
);

INSERT INTO lists VALUES
  (721, 1, 13.3),
  (721, 4, 6.7),
  (721, 5, 1.2),
  (721, 2, 2.3),
  (721, 3, 6.5),
  (987, 5, 2.5),
  (987, 3, 2.5),
  (987, 1, 2.5),
  (987, 2, 2.4),
  (987, 4, 10.1),
  (123, 1, 9);

-- retrieve the filenode
SELECT relname, relfilenode
FROM   pg_class
WHERE  relname='lists';
--
SHOW data_directory;
SELECT pg_relation_filepath('lists');
