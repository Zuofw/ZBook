# 如果存在就
use zfw_book;
create table user(
                     id int auto_increment primary key,
                     user_name varchar(255),
                     password varchar(255)
) engine=innodb default charset=utf8;