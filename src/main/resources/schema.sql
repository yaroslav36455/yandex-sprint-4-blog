CREATE TABLE IF NOT EXISTS POST
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR,
    image       VARCHAR,
    text        VARCHAR,
    likes_count INT,
    tags        JSON
);

CREATE TABLE IF NOT EXISTS comment
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    text    VARCHAR,
    post_id BIGINT,
    FOREIGN KEY (post_id) REFERENCES POST (id)
);
