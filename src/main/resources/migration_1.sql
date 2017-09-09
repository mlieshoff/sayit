drop table if exists `thread`;
create table `thread` (
    `id` bigint not null,
    `created` timestamp default now(),
    `user` varchar(50) not null
);

drop table if exists `post`;
create table `post` (
    `id` bigint not null,
    `thread` bigint not null,
    `created` timestamp default now(),
    `user` varchar(50) not null,
    `comment` text default null
);

drop table if exists `media`;
create table `media` (
    `reference` bigint not null,
    `filename` varchar(255) not null,
    `shard` int default null
);

drop table if exists `shard`;
create table `shard` (
    `id` int not null,
    `host` varchar(255),
    `port` int,
    `folder` varchar(255),
    `user` varchar(255),
    `password` varchar(255),
    PRIMARY KEY (`id`)
);

insert into `shard` values(1, 'sayit2016.lima-ftp.de', 21, 'sayit2016.lima-city.de', 'sayit2016', 'CBqE92ga6E');