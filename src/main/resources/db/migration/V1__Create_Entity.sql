-- users
create sequence users_sequence
    start with 100000
    increment by 1
    cache 50;

create table users
(
    id           bigint         not null default nextval('users_sequence'),
    login        varchar(100)   not null,
    password     varchar(256)   not null,
    email        varchar(256)   not null,
    phone_number varchar(15),
    photo_url    varchar(256),
    ----------------------------------------------------------------------
    constraint users_id_pk          primary key (id),
    constraint users_login_uq       unique(login),
    constraint users_email_uq       unique(email)
);

comment on table  users              is 'Таблица пользователей';
comment on column users.id           is 'ID пользователя';
comment on column users.password     is 'Пароль пользователя';
comment on column users.email        is 'Email пользователя';
comment on column users.phone_number is 'Телефонный номер пользователя';
comment on column users.photo_url    is 'Ссылка на фотографию профиля пользователя';


--point
create sequence point_sequence
    start with 100000
    increment by 1
    cache 50;

create table point
(
    id        bigint            not null default nextval('point_sequence'),
    lat       double precision  not null,
    lng       double precision  not null,
    name      VARCHAR(255)      not null,
    country   VARCHAR(100)      not null,
    state     VARCHAR(100)      not null,
    osm_value VARCHAR(255)      not null,
    ----------------------------------------------------------------------
    constraint point_id_pk          primary key (id),
    constraint point_lat_lng_uq     unique (lat, lng)
);

comment on table  point              is 'Таблица точки на карте';
comment on column point.id           is 'ID точки';
comment on column point.lat          is 'Широта точки';
comment on column point.lng          is 'Долгота точки';
comment on column point.name         is 'Наименование точки';
comment on column point.country      is 'Страна точки';
comment on column point.state        is 'State точки'; --республика
comment on column point.osm_value    is 'Тип точки';

--trip status
create type trip_status as enum ('available', 'completed', 'cancelled');

comment on type trip_status is 'Enum для статуса поездки';

--route
create sequence route_sequence
    start with 100000
    increment by 1
    cache 50;

create table route
(
    id              bigint                  not null        default nextval('route_sequence'),
    user_id         bigint,
    start_point_id  bigint                  not null,
    finish_point_id bigint                  not null,
    distance        decimal(10, 2)          default 0,
    ------------------------------------------------------------------------------------------------------
    constraint route_id_pk              primary key(id),
    constraint route_user_id_fk         foreign key(user_id)         references users(id) on delete set null,
    constraint route_start_point_id_fk  foreign key(start_point_id)  references point(id) on delete cascade,
    constraint route_finish_point_id_fk foreign key(finish_point_id) references point(id) on delete cascade

);

comment on table  route                 is 'Таблица маршрута';
comment on column route.id              is 'ID маршрута';
comment on column route.distance        is 'Протяженность маршрута в км';
comment on column route.user_id         is 'ID создателя маршрута';
comment on column route.start_point_id  is 'ID начальной точки маршрута';
comment on column route.finish_point_id is 'ID конечной точки маршрута';


--trip
create sequence trip_sequence
    start with 100000
    increment by 1
    cache 50;

create table trip
(
    id              bigint              not null default nextval('trip_sequence'),
    user_id         bigint,
    route_id        bigint              not null,
    trip_date_time  timestamp           not null,
    available_seats int                 not null,
    price           decimal(10, 2)      not null,
    status          trip_status         not null,
    ---------------------------------------------------------------------------------------------
    constraint trip_id_pk           primary key(id),
    constraint trip_user_id_fk      foreign key (user_id)   references users(id) on delete set null,
    constraint trip_route_id_fk     foreign key (route_id)  references route(id) on delete cascade
);

comment on table  trip                 is 'Таблица поездок';
comment on column trip.id              is 'ID поездки';
comment on column trip.user_id         is 'ID создателя поездки';
comment on column trip.route_id        is 'ID маршрута поездки';
comment on column trip.trip_date_time  is 'Время начала поездки';
comment on column trip.available_seats is 'Количество свободных мест';
comment on column trip.price           is 'Цена билета';
comment on column trip.status          is 'Статус поездки';

--review
create sequence review_sequence
    start with 100000
    increment by 1
    cache 50;

create table review
(
    id          bigint          not null default nextval('review_sequence'),
    user_id     bigint          not null,
    trip_id     bigint          not null,
    rating      int             not null    check (rating between 1 and 5),
    description text            check       (length(description) <= 350),
    ---------------------------------------------------------------
    constraint review_id_pk         primary key (id),
    constraint review_user_id_fk    foreign key (user_id)   references users(id)    on delete cascade,
    constraint review_trip_id_fk    foreign key (trip_id)   references trip(id)     on delete cascade

);

comment on table  review                 is 'Таблица отзывов';
comment on column review.id              is 'ID отзыва';
comment on column review.user_id         is 'ID создателя отзыва';
comment on column review.trip_id         is 'ID поездки, для которого был оставлен отзыв';
comment on column review.rating          is 'Рейтинг';
comment on column review.description     is 'Текст отзыва';


-- trip and participants - m2m table
create table trip_participants
(
    trip_id     bigint,
    user_id     bigint,
    ------------------------------------------------------------------------------------------------------------------
    constraint trip_participants_pk             primary key (trip_id, user_id),
    constraint trip_participants_trip_id_fk     foreign key (trip_id)           references trip(id) on delete cascade,
    constraint trip_participants_user_id_fk     foreign key (user_id)           references users(id) on delete cascade
);
comment on table  trip_participants              is 'Таблица для связи m2m Trip и Users';
comment on column trip_participants.user_id      is 'ID пользователя';
comment on column trip_participants.trip_id      is 'ID поездки';


--route and intermediate points
create table route_intermediate_points
(
    route_id    bigint          not null,
    point_id    bigint          not null,
    sequence    int             not null ,
    ------------------------------------------------------------------------
    constraint route_intermediate_points_pk primary key(route_id, point_id, sequence),
    constraint route_intermediate_points_route_id_fk foreign key(route_id) references route(id) on delete cascade,
    constraint route_intermediate_points_point_id_fk foreign key (point_id) references point(id) on delete cascade
);

comment on table route_intermediate_points              is 'Таблица для связи m2m Route и Point';
comment on column route_intermediate_points.route_id    is 'ID маршрута';
comment on column route_intermediate_points.point_id    is 'ID точки';
comment on column route_intermediate_points.sequence    is 'Порядковый номер промежуточной точки';
