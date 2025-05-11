package by.tyv.service;

import by.tyv.model.entity.Post;

import java.util.List;

public interface PostService {
    List<Post> getPosts();
    Post getPostById(int id);
}
