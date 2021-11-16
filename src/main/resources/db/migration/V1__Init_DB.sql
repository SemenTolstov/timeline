create table message
(
    id                    int8          not null,
    head                  varchar(255)  not null,
    text                  varchar(2048) not null,
    date_of_adding_as_utc TIMESTAMP     not null,
    user_id               int82         not null,
    primary key (id)
);

create table users
(
    id       int8         not null,
    uuid     UUID         not null,
    password varchar(255) not null,
    login    varchar(255) not null,
    primary key (id)
);

alter table if exists message
    add constraint message_user_fk
    foreign key (user_id) references users;