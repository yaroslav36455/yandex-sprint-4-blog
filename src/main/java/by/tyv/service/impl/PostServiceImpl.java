package by.tyv.service.impl;

import by.tyv.model.entity.Post;
import by.tyv.model.view.PostPage;
import by.tyv.repository.PostRepository;
import by.tyv.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository repository;

    @Override
    @Transactional
    public PostPage getPostPage(String search, int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public List<Post> getPosts() {
        return repository.findAll();
    }

    @Override
    public Post getPostById(int id) {
        return null;
    }
}
