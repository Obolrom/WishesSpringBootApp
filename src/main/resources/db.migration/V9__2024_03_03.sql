create table if not exists message (
    id serial primary key not null,
    message text,
    created_at bigint default extract(epoch from now()),
    modified_at bigint,
    deleted boolean not null default false,
    author_id serial not null,
    receiver_id serial not null,

    constraint message_author_fk foreign key (author_id) references users(user_id),
    constraint message_receiver_fk foreign key (receiver_id) references users(user_id)
);