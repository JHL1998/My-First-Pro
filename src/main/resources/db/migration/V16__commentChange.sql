alter table comment modify content varchar(1024) character set "utf8";
alter table notification modify outer_title varchar(256) character set "utf8";
alter table notification modify notifier_name varchar(100) character set "utf8";
