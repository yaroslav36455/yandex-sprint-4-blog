package by.tyv.util;

import by.tyv.model.bo.Comment;
import by.tyv.model.bo.Post;

import java.util.List;

public class DataUtil {

    static public Post getPost1() {
        return new Post()
                .setId(1L)
                .setTitle("Title-1")
                .setImage("trees.png")
                .setText("some-text")
                .setComments(List.of(
                        new Comment(1L, "Comment 1 for post 1", 1L),
                        new Comment(2L, "Comment 2 for post 1", 1L)))
                .setLikesCount(1)
                .setTags(List.of("tag1", "tag2"));
    }

    static public Post getPost2() {
        return new Post()
                .setId(2L)
                .setTitle("Title-2")
                .setImage("sedana.png")
                .setText("some-text")
                .setComments(List.of(
                        new Comment(3L, "Comment 1 for post 2", 2L),
                        new Comment(4L, "Comment 2 for post 2", 2L),
                        new Comment(5L, "Comment 2 for post 2", 2L)))
                .setLikesCount(2)
                .setTags(List.of("tag3", "tag2"));
    }

    static public Post getPost3() {
        return new Post()
                .setId(3L)
                .setTitle("Title-3")
                .setImage("flowers.png")
                .setText("some-text")
                .setComments(List.of())
                .setLikesCount(3)
                .setTags(List.of("tag1", "tag3"));
    }

    static public Post getPost4() {
        return new Post()
                .setId(4L)
                .setTitle("Title-4")
                .setImage("mushrooms.png")
                .setText("some-text")
                .setComments(List.of(
                        new Comment(6L,  "Comment 1 for post 4", 4L),
                        new Comment(7L,  "Comment 2 for post 4", 4L),
                        new Comment(8L,  "Comment 3 for post 4", 4L),
                        new Comment(9L,  "Comment 4 for post 4", 4L),
                        new Comment(10L, "Comment 5 for post 4", 4L),
                        new Comment(11L, "Comment 6 for post 4", 4L),
                        new Comment(12L, "Comment 7 for post 4", 4L)))
                .setLikesCount(4)
                .setTags(List.of("tag1", "tag2"));
    }

    static public Post getPost5() {
        return new Post()
                .setId(5L)
                .setTitle("Title-5")
                .setImage("dark-planet.png")
                .setText("some-text")
                .setComments(List.of())
                .setLikesCount(5)
                .setTags(List.of("tag1", "tag2"));
    }

    static public Post getPost6() {
        return new Post()
                .setId(6L)
                .setTitle("Title-6")
                .setImage("fish.png")
                .setText("some-text")
                .setComments(List.of())
                .setLikesCount(6)
                .setTags(List.of("tag1", "tag4"));
    }

    static public Post getPost7() {
        return new Post()
                .setId(7L)
                .setTitle("Title-7")
                .setImage("storm.png")
                .setText("some-text")
                .setComments(List.of(new Comment(13L, "Comment 1 for post 7", 7L)))
                .setLikesCount(6)
                .setTags(List.of("tag1", "tag2", "tag3"));
    }

    static public Post getPost8() {
        return new Post()
                .setId(8L)
                .setTitle("Title-8")
                .setImage("insects.png")
                .setText("some-text")
                .setComments(List.of())
                .setLikesCount(7)
                .setTags(List.of("tag1", "tag2"));
    }
}
