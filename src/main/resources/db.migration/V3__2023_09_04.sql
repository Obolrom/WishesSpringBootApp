alter table expenses
drop constraint expense_user_constraint,
add constraint expense_user_constraint
    foreign key (user_id) references users(user_id)
    on delete CASCADE;
