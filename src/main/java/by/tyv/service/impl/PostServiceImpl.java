package by.tyv.service.impl;

import by.tyv.model.entity.Post;
import by.tyv.repository.PostRepository;
import by.tyv.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository repository;

    @Override
    public List<Post> getPosts() {
        return repository.findAll();
    }

    @Override
    public Post getPostById(int id) {
        return null;
    }
}
