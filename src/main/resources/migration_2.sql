drop table if exists `time_project`;
create table `time_project` (
    `id` bigint not null,
    `user_hash` bigint not null,
    `name` varchar(50) not null
);

drop table if exists `time_user`;
create table `time_user` (
    `username` bigint not null,
    `email` varchar(320) default null,
    `password_hash` bigint not null,
    `created_at` timestamp default now()
);

drop table if exists `time_measure`;
create table `time_measure` (
    `user_hash` bigint not null,
    `project_hash` bigint not null,
    `stamp` timestamp not null,
    `action` tinyint not null
);