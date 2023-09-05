create table if not exists categories(
    id serial primary key,
    name varchar(64) not null
);

insert into categories (name) select category from expenses;

alter table expenses
add column category_id serial not null;

update expenses
set category_id = (select c.id from categories c where c.name = expenses.category)
where exists (select true from categories c where c.name = expenses.category);
