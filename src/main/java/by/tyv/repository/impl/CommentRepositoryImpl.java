package by.tyv.repository.impl;

import by.tyv.model.entity.Comment;
import by.tyv.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Comment> findCommentsByPostId(long postId) {
        return jdbcTemplate.query("SELECT id, text FROM comment WHERE post_id = ?", rs -> {
            List<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                comments.add(new Comment(
                       rs.getLong("id"),
                       rs.getString("text"),
                       postId
                ));
            }
            return comments;
        }, postId);
    }

    @Override
    public List<Comment> findCommentsByPostId(List<Long> postIds) {
        String placeholder = postIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        return jdbcTemplate.query("SELECT id, text, post_id FROM comment WHERE post_id IN (" + placeholder + ")", rs -> {
            List<Comment> comments = new ArrayList<>();
            while (rs.next()) {
                comments.add(new Comment(
                       rs.getLong("id"),
                       rs.getString("text"),
                       rs.getLong("post_id")
                ));
            }

            return comments;
        }, postIds.toArray());
    }

    @Override
    public void deleteByPostId(long id) {
        jdbcTemplate.update("DELETE FROM comment WHERE post_id = ?", id);
    }
}
