CREATE TABLE IF NOT EXISTS POST
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR NOT NULL,
    image       VARCHAR,
    text        VARCHAR NOT NULL,
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