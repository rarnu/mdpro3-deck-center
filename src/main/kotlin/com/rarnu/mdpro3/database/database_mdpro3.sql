create database mdpro3;

-- 卡组表
create table deck
(
    deck_id          varchar(64)  not null primary key,   -- 主键ID
    deck_contributor varchar(128) not null,               -- 贡献者
    deck_name        varchar(128) not null,               -- 卡组名称
    deck_rank        int          not null default 0,     -- 流行程度，用于排序
    deck_like        int          not null default 0,     -- 点赞数
    deck_upload_date datetime     not null default NOW(), -- 卡组上传时间
    deck_update_date datetime     not null default NOW(), -- 卡组的更新时间
    deck_cover_card1 bigint       not null default 0,     -- 卡组封面卡1
    deck_cover_card2 bigint       not null default 0,     -- 卡组封面卡2
    deck_cover_card3 bigint       not null default 0,     -- 卡组封面卡3
    deck_main_serial varchar(512) not null default '',    -- 卡组的主要系列，通过封面卡获取
    deck_ydk         longtext                             -- YDK 内容
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

-- 2024.07.12
alter table deck
    add column is_delete int not null default 0;
-- 是否已删除,0未删，1:已删


-- 2024.09.26

-- 用户配置表
create table user_config
(
    user_id bigint not null primary key, -- 用户 ID，主键
    config  longtext,                    -- 配置信息，以 json 保存
    extra1  longtext,                    -- 额外信息1
    extra2  longtext,                    -- 额外信息2
    extra3  longtext,                    -- 额外信息3
    extra4  longtext,                    -- 额外信息4
    extra5  longtext                     -- 额外信息5
) character set utf8mb4;

-- 创建额外的索引
create index idx_pub_del on deck (is_delete, is_public);
create index idx_name on deck (deck_name);
create index idx_contributor on deck (deck_contributor);
CREATE INDEX idx_userid ON deck (user_id);
CREATE INDEX idx_update ON deck (deck_update_date);
create index idx_like on deck (deck_like);

-- 2024.10.16

-- 残局表
create table puzzle
(
    id           bigint       not null primary key auto_increment, -- 主键
    name         varchar(256) not null,                            -- 残局的名称
    user_id      bigint       not null default 0,                  -- 上传者的用户 id
    contributor  varchar(128) not null default '',                 -- 上传者的名字
    lua_script   text,                                             -- lua 脚本
    message      text,                                             -- message
    solution     text,                                             -- solution
    cover_card   bigint,                                           -- 第一张卡
    audited      int          not null default 0,                  -- 审核状态:0:未审核，1:已通过，2:已拒绝
    publish_date datetime     not null default now()               -- 发布时间
) character set utf8mb4;

-- 残局通关表
create table puzzle_pass
(
    puzzle_id bigint   not null,               -- 残局id
    user_id   bigint   not null,               -- 通关的用户id
    pass_time datetime not null default now(), -- 通关时间
    primary key (puzzle_id, user_id)
) character set utf8mb4;

create unique index idx_name_user on puzzle(name, user_id);
create index idx_contributor on puzzle(contributor);
create index idx_audited on puzzle(audited);

-- 用户角色表，用于判定管理员权限
create table user (
    user_id bigint not null primary key, -- 用户ID，来源MC
    role_id int not null default 0 -- 用户角色，默认是0，即没有权限，管理员设置为100
) character set utf8mb4;

