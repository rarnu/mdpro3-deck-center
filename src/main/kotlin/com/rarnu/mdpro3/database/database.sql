create database mdpro3;

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