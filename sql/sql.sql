-- auto-generated definition
create schema pensioninsurance collate utf8mb4_0900_ai_ci;

create table social
(
    bureau_address  varchar(255) not null,
    bureau_name     varchar(255) not null,
    bureau_security varchar(255) null
);
-- auto-generated definition
create table sponsor
(
    sponsor_address varchar(255) not null comment '雇主地址'
        primary key,
    sponsor_name    varchar(255) null comment '雇主名字',
    sign_security   varchar(512) null
);
create table user
(
    id               bigint auto_increment comment '主键'
        primary key,
    name             varchar(255)      null comment '用户姓名',
    employee_address varchar(255)      not null comment '用户地址',
    sign_security    varchar(255)      null comment '用户密钥',
    user_type        tinyint default 1 null comment '用户类型 0-管理员 1-员工 2-社保局 3-雇主',
    phone            varchar(11)       null comment '手机号码',
    sex              tinyint           null comment '性别 0-男 1-女',
    age              int               null comment '年龄',
    employer         varchar(255)      null comment '雇主名字',
    idNumber         varchar(18)       null comment '身份证号码',
    avatar           varchar(500)      null comment '头像',
    craete_time      datetime          null comment '创建时间',
    update_time      datetime          null comment '更新时间'
)