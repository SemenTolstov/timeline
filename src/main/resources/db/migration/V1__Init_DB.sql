create sequence usr_seq;
create table users
(
    id       int8        not null default nextval('usr_seq'),
    uuid     UUID        not null,
    password varchar     not null,
    login    varchar(10) not null unique,
    primary key (id)
);
alter sequence usr_seq owned by users.id;

create sequence msg_seq;
create table message
(
    id                    int8          not null default nextval('msg_seq'),
    head                  varchar(10)   not null,
    text                  varchar(1000) not null,
    date_of_adding_as_utc TIMESTAMP     not null,
    user_id               int8          not null,
    primary key (id)
);
alter sequence msg_seq owned by message.id;

alter table if exists message
    add constraint message_user_fk
    foreign key (user_id) references users;