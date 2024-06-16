create database mdpro3;

-- 卡组表
create table deck
(
    deck_id          varchar(64)  not null primary key,               -- 主键ID
    deck_contributor varchar(128) not null,                           -- 贡献者
    deck_name        varchar(128) not null,                           -- 卡组名称
    deck_rank        int          not null default 0,                 -- 流行程度，用于排序
    deck_like        int          not null default 0,                 -- 点赞数
    deck_upload_date datetime     not null default NOW(),             -- 卡组上传时间
    deck_update_date datetime              default null default null, -- 卡组的更新时间
    deck_cover_card1 bigint       not null default 0,                 -- 卡组封面卡1
    deck_cover_card2 bigint       not null default 0,                 -- 卡组封面卡2
    deck_cover_card3 bigint       not null default 0,                 -- 卡组封面卡3
    deck_main_serial varchar(512) not null default '',                -- 卡组的主要系列，通过封面卡获取
    deck_ydk         longtext                                         -- YDK 内容
) character set utf8mb4;

alter table deck
    add column deck_case bigint not null default 0 after deck_cover_card3;
alter table deck
    add column deck_protector bigint not null default 0 after deck_case;

-- 敏感词表
create table sensitive_word
(
    word varchar(64) primary key
) character set utf8mb4;

-- 2024.06.10
alter table deck
    add column user_id bigint not null default 0;
alter table deck
    add column is_public int not null default 0; -- 是否公开 1公开，0不公开
alter table deck
    add column description longtext; -- 卡组描述

create table deck_id
(
    id varchar(64) not null primary key -- 卡组id主键
) character set utf8mb4;

insert into deck_id
select deck.deck_id
from deck;

-- 2024.06.16
create table client_source
(
    id          bigint primary key auto_increment, -- 主键
    user_agent  varchar(512) not null default '',  -- UA
    source      varchar(128) not null default '',  -- 请求来源
    ip_address  varchar(128) not null default '',  -- IP
    call_api    varchar(128) not null default '',  -- 调用的 API
    create_time datetime     not null default now()
) character set utf8mb4;