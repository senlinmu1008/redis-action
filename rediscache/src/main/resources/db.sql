DROP TABLE IF EXISTS user;
CREATE TABLE user (
    id bigint(20) NOT NULL AUTO_INCREMENT,
    name varchar(64) COMMENT '姓名',
    age int(4) COMMENT '年龄',
    PRIMARY KEY (id)
)COMMENT = '用户表';

insert into user values (1, '赵晓斌', 28);
insert into user values (2, '李白', 22);
insert into user values (3, '宋老三', 30);