CREATE TABLE IF NOT EXISTS POST
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR,
    image       VARCHAR,
    text        VARCHAR,
    likes_count INT NOT NULL DEFAULT 0,
    tags        JSON NOT NULL DEFAULT '[]'
);

CREATE TABLE IF NOT EXISTS comment
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    text    VARCHAR,
    post_id BIGINT NOT NULL,
    FOREIGN KEY (post_id) REFERENCES POST (id)
);

insert into post (title, image, text, likes_count, tags)
values ('Title-1', 'image path', 'some-text', 1, '["tag1", "tag2"]'),
       ('Title-2', 'image path', 'some-text', 2, '["tag3", "tag2"]'),
       ('Title-3', 'image path', 'some-text', 3, '["tag1", "tag3"]'),
       ('Title-4', 'image path', 'some-text', 4, '["tag1", "tag2"]'),
       ('Title-5', 'image path', 'some-text', 5, '["tag1", "tag2"]'),
       ('Title-6', 'image path', 'some-text', 6, '["tag1", "tag2", "tag3"]'),
       ('Title-7', 'image path', 'some-text', 7, '["tag1", "tag2"]');
