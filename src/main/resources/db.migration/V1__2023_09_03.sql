create table users(
    user_id serial primary key,
    username varchar(64) not null unique,
    photo_url varchar(2048) not null,
    description varchar(512) null,
    date_of_birth timestamp
);