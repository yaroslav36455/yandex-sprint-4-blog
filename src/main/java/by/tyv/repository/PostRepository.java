package by.tyv.repository;

import by.tyv.model.entity.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAllPaging(int offset, int amount);
    List<Post> findAllPaging(String search, int offset, int amount);
}
