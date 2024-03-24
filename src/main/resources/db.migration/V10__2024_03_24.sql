create table if not exists chat (
    id serial primary key not null,
    title varchar(255) not null unique
);

alter table message
    add column chat_id bigint null;

alter table message
    add constraint message_chat_fk foreign key (chat_id) references chat(id);