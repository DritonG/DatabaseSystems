-- Andreas Schmid (3087156)
-- Tobias Stumpp (3798377)

DROP TABLE IF EXISTS Parts, Supply;
CREATE TABLE Parts(
  pnum INTEGER,
  qoh INTEGER
);
CREATE TABLE Supply(
  pnum INTEGER,
  quan INTEGER,
  shipdate date
);

INSERT INTO Parts
VALUES ( 3, 6),
       (10, 1),
       ( 8, 0);

INSERT INTO Supply
VALUES ( 3, 4,  '7-3-79'),
       ( 3, 2, '10-1-78'),
       (10, 1,  '6-8-78'),
       (10, 2, '8-10-81'),
       ( 8, 5,  '5-7-83');
  

SELECT          pnum
FROM            Parts
LEFT OUTER JOIN (SELECT pnum, COUNT(shipdate) AS ct
                 FROM   Supply
                 WHERE  shipdate < 1-1-80
                 GROUP BY pnum) T ON (Parts.pnum = T.pnum)
WHERE           (Parts.qoh = 0 AND
                T.count IS NULL) OR
                Parts.qoh = T.ct;
