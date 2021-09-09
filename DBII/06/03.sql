-- Andreas Schmied (3087156), Tobias Stumpp (3798377)
--
-- Übungsblatt 6, Datenbanksysteme II
--- Aufgabe 3


-- Repraesentiert Punkte in einem 2-dim Raum.
DROP TABLE points;
CREATE TABLE IF NOT EXISTS points(
  x int,
  y int
);
-- Belege alle moeglichen Punkte im Bereich x=[0,99], y=[0,99]
INSERT INTO points(x, y) (
  SELECT s1, s2
  FROM   GENERATE_SERIES(0,99) s1,
         GENERATE_SERIES(0,99) s2
);

-- Gegebene Funktion: Abbildung 2, Aufgabenblatt
CREATE OR REPLACE FUNCTION point_to_z(x int, y int)
RETURNS int AS $$
DECLARE
z bit varying := '';
BEGIN
FOR i IN 0..6 LOOP
z := (x >> i) :: bit(1) || (y >> i) :: bit(1) || z;
END LOOP;
RETURN z :: bit(14) :: int;
END
$$ LANGUAGE PLPGSQL IMMUTABLE;
--

-- Erzeuge Z-Code Index
CREATE INDEX zindex ON points USING btree (point_to_z(x,y));
-- Erzeuge composite search-key Index: points <x,y>
CREATE INDEX cindex ON points USING btree (x,y);

-- 
CREATE EXTENSION IF NOT EXISTS pageinspect;


-- Z-Code Index von Punkten der Page 2
COPY (
  SELECT     ctid, x, y
  FROM       bt_page_items('zindex',2) i
  INNER JOIN points p ON (i.ctid = p.ctid)
  ORDER BY   ctid
) TO '/tmp/zindex.csv';

-- Composite search-key Index von Punkten der Page 2
COPY (
  SELECT     ctid, x, y
  FROM       bt_page_items('cindex',2) i
  INNER JOIN points p ON (i.ctid = p.ctid)
  ORDER BY   ctid
) TO '/tmp/cindex.csv';

