show databases;
use photo_app;
show tables;

select * from users;
select * from addresses;
select * from password_reset_tokens;

delete from users;
delete from addresses;
delete from password_reset_tokens;

drop table users;
drop table addresses;