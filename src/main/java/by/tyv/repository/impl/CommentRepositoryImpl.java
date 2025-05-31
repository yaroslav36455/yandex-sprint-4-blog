package by.tyv.repository.impl;

import by.tyv.model.entity.Comment;
import by.tyv.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
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
                comments.add(mapToComment(rs));
            }

            return comments;
        }, postIds.toArray());
    }

    @Override
    public void deleteByPostId(long id) {
        jdbcTemplate.update("DELETE FROM comment WHERE post_id = ?", id);
    }

    @Override
    public void addCommentByPostId(long postId, String comment) {
        jdbcTemplate.update("INSERT INTO comment (text, post_id) VALUES (?, ?)", comment, postId);
    }

    @Override
    public Optional<Comment> findById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT id, text, post_id FROM comment WHERE id = ?",
                    (rs, rowNum) -> mapToComment(rs), id));
        } catch (EmptyResultDataAccessException e) {
            log.warn("Comment with id {} not found", id);
            return Optional.empty();
        }
    }

    private Comment mapToComment(ResultSet resultSet) throws SQLException {
        return new Comment(
                resultSet.getLong("id"),
                resultSet.getString("text"),
                resultSet.getLong("post_id")
        );
    }
}
