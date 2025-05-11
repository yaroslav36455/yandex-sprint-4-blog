package by.tyv.repository.impl;

import by.tyv.model.entity.Post;
import by.tyv.repository.PostRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    @Override
    public List<Post> findAll() {
        return List.of(
          new Post(1L, "Title", "Text", List.of(), 0, List.of("tag1", "tag2"))
        );
    }
}
