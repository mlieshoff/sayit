drop table if exists `rcf_user`;
create table `rcf_user` (
    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `nickname` varchar(255),
    `tag` varchar(16),
    `role` varchar(24) not null default 'Member',
    `joinedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;
drop table if exists `rcf_clan_week`;
create table `rcf_clan_week` (
    `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
    `year` int(10) not null,
    `week` int(10) not null,
    PRIMARY KEY (`id`),
    UNIQUE KEY `year_week` (`year`, `week`)
) DEFAULT CHARSET=utf8;
drop table if exists `rcf_spend_week_user`;
create table `rcf_spend_week_user` (
    `user` int(10) not null,
    `clanWeek` int(10) not null,
    `amount` int(10) not null,
    PRIMARY KEY (`user`, `clanWeek`)
) DEFAULT CHARSET=utf8;
drop table if exists `rcf_chest_week_user`;
create table `rcf_chest_week_user` (
    `user` int(10) not null,
    `clanWeek` int(10) not null,
    `amount` int(10) not null,
    PRIMARY KEY (`user`, `clanWeek`)
) DEFAULT CHARSET=utf8;
