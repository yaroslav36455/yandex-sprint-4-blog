insert into post (title, image, text, likes_count, tags)
values ('Title-1', 'trees.png', 'some-text', 1, '["tag1", "tag2"]'),
       ('Title-2', 'sedana.png', 'some-text', 2, '["tag3", "tag2"]'),
       ('Title-3', 'flowers.png', 'some-text', 3, '["tag1", "tag3"]'),
       ('Title-4', 'mushrooms.png', 'some-text', 4, '["tag1", "tag2"]'),
       ('Title-5', 'dark-planet.png', 'some-text', 5, '["tag1", "tag2"]'),
       ('Title-6', 'fish.png', 'some-text', 6, '["tag1", "tag4"]'),
       ('Title-7', 'storm.png', 'some-text', 7, '["tag1", "tag2", "tag3"]'),
       ('Title-8', 'insects.png', 'some-text', 8, '["tag1", "tag2"]');

insert into comment (post_id, text)
values (1, 'Comment 1 for post 1'),
       (1, 'Comment 2 for post 1'),
       (2, 'Comment 1 for post 2'),
       (2, 'Comment 2 for post 2'),
       (2, 'Comment 3 for post 2'),
       (4, 'Comment 1 for post 4'),
       (4, 'Comment 2 for post 4'),
       (4, 'Comment 3 for post 4'),
       (4, 'Comment 4 for post 4'),
       (4, 'Comment 5 for post 4'),
       (4, 'Comment 6 for post 4'),
       (4, 'Comment 7 for post 4'),
       (7, 'Comment 1 for post 7');