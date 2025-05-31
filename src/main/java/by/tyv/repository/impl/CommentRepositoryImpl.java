package by.tyv.repository.impl;

import by.tyv.model.entity.Comment;
import by.tyv.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Override
    public void addCommentByPostId(long postId, String comment) {
        jdbcTemplate.update("INSERT INTO comment (text, post_id) VALUES (?, ?)", comment, postId);
    }

    @Override
    public Optional<Comment> findCommentById(long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT id, text, post_id FROM comment WHERE id = ?", new RowMapper<Comment>() {
            @Override
            public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Comment(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getLong("post_id")
                );
            }
        }, id));
    }
}
