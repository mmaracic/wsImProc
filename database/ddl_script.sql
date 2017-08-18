create schema ws_im_proc;

create sequence ws_im_proc.seq;

/*
drop view ws_im_proc.user_image_conversions;
drop index image_points_image_conversions;
drop index image_conversions_date;
drop index image_conversions_user;
drop table ws_im_proc.image_points;
drop table ws_im_proc.image_conversions;
drop table ws_im_proc.users;
*/

create table ws_im_proc.users(
	id bigint default nextval('ws_im_proc.seq'),
    username varchar unique not null,
    password varchar not null,
    last_access timestamp
);
alter table ws_im_proc.users add constraint users_pk primary key(id);

create table ws_im_proc.image_conversions(
    id bigint default nextval('ws_im_proc.seq'),
    access_date timestamp not null,
    user_id bigint not null,
    im_width int not null,
    im_height int not null,
    original_image bytea not null,
    vector_image bytea not null
);
alter table ws_im_proc.image_conversions add constraint image_conversions_pk primary key(id);
alter table ws_im_proc.image_conversions add constraint image_conversions_users foreign key(user_id) references ws_im_proc.users(id);

create index image_conversions_date on ws_im_proc.image_conversions(access_date);
create index image_conversions_user on ws_im_proc.image_conversions(user_id);

create table ws_im_proc.image_points(
    id bigint default nextval('ws_im_proc.seq'),
    image_id bigint not null,
    x int not null,
    y int not null
);
alter table ws_im_proc.image_points add constraint image_points_pk primary key(id);
alter table ws_im_proc.image_points add constraint image_points_image_conversions foreign key(image_id) references ws_im_proc.image_conversions(id);

create index image_points_image_conversions on ws_im_proc.image_points(image_id);

create view ws_im_proc.user_image_conversions as
	select u.username, im.access_date, im.im_width, im.im_height, count(pt.image_id)
    from ws_im_proc.users u
    left outer join ws_im_proc.image_conversions im on u.id = im.user_id
    left outer join ws_im_proc.image_points pt on im.id = pt.image_id
    group by u.username, im.access_date, im.im_width, im.im_height, pt.image_id;