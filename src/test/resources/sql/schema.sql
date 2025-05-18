CREATE TABLE IF NOT EXISTS post
(
    id          bigint auto_increment primary key,
    title       varchar,
    image       varchar,
    text        varchar,
    likes_count int,
    tags        json
);

CREATE TABLE IF NOT EXISTS comment
(
    id      bigint auto_increment primary key,
    text    varchar,
    post_id bigint,
    foreign key (post_id) references POST (id)
);
