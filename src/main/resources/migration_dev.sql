drop table if exists `rcf2_tag`;
create table `rcf2_tag` (
    `userHash` int(10) not null,
    `tag` varchar(16),
    `current` bool default 1,
    `modifiedAt` timestamp null default null
) DEFAULT CHARSET=utf8;
drop table if exists `rcf2_nick`;
create table `rcf2_nick` (
    `userHash` int(10) not null,
    `nick` varchar(255),
    `current` bool default 1,
    `modifiedAt` timestamp null default null
) DEFAULT CHARSET=utf8;
drop table if exists `rcf2_role`;
create table `rcf2_role` (
    `userHash` int(10) not null,
    `role` int(10) not null,
    `current` bool default 1,
    `modifiedAt` timestamp null default null
) DEFAULT CHARSET=utf8;
drop table if exists `rcf2_donations`;
create table `rcf2_donations` (
    `userHash` int(10) not null,
    `donations` int(10) not null,
    `current` bool default 1,
    `modifiedAt` timestamp null default null
) DEFAULT CHARSET=utf8;
drop table if exists `rcf2_crowns`;
create table `rcf2_crowns` (
    `userHash` int(10) not null,
    `crowns` int(10) not null,
    `current` bool default 1,
    `modifiedAt` timestamp null default null
) DEFAULT CHARSET=utf8;