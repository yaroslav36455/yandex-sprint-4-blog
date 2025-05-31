package by.tyv.repository;

import by.tyv.model.entity.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository {
    List<Comment> findCommentsByPostId(long postId);
    List<Comment> findCommentsByPostId(List<Long> postIds);
    void deleteByPostId(long id);
    void addCommentByPostId(long postId, String comment);
}
