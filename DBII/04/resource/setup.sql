-- Enable buffer cache statistics view in this database
drop extension pageinspect;
create extension pageinspect;

-- Create the test relation with a specific fill factor
drop table if exists crel;
create table crel(key integer not null, 
		  payload varchar(100) not null)
	      with (fillfactor = 100);

-- Copy data for table crel from file. IMPORTANT: Insert the ABSOLUTE path to the file
-- on your machine.
copy crel from 'INSERTPATH/initial.tbl' with (format csv, delimiter '|');

-- create an unclustered index on attribute key.
drop index if exists crel_key_idx;
create index crel_key_idx on crel(key);

-- cluster the table according to the index
cluster verbose crel using crel_key_idx;
analyze;

create or replace function page_from_ctid(id tid) returns integer as
$$
begin
	return substring(id::text from '\((\d+),')::int;
end;
$$ language plpgsql;
