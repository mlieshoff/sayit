drop table if exists `elite_user`;
create table `elite_user` (
    `userId` varchar(255) not null,
    `createdAt` timestamp not null,
    `lastLogin` timestamp not null
);