package by.tyv.service;

import by.tyv.model.entity.Post;
import by.tyv.model.view.PostPage;

import java.util.List;

public interface PostService {
    List<Post> getPosts();
    Post getPostById(int id);

    PostPage getPostPage(String search, int pageNumber, int pageSize);
}
