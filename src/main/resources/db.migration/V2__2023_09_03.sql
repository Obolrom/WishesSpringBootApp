create table if not exists expenses(
    expense_id serial primary key,
    expense_sum double precision not null,
    category varchar(128) not null,
    timestamp timestamp not null,
    user_id serial not null,
    description varchar(256) null,

    constraint expense_user_constraint foreign key (user_id) references users(user_id)
);