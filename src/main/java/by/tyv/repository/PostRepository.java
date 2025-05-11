package by.tyv.repository;

import by.tyv.model.entity.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();
}
