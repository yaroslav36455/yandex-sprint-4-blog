package by.tyv.repository;

import by.tyv.model.entity.Post;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    List<Post> findAllPaging(int offset, int amount);
    List<Post> findAllPaging(String search, int offset, int amount);
    Optional<Post> findById(Long id);
    Optional<String> findPostImageNameById(long id);
    long save(Post post) throws JsonProcessingException;
    void addLike(long id);
    void dislikeLike(long id);
}
