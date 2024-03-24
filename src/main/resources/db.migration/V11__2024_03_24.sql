alter table message
    drop constraint message_receiver_fk;

alter table message
    alter column receiver_id type bigint;

alter table message
    add constraint message_receiver_fk
    foreign key (receiver_id)
    references users(user_id);