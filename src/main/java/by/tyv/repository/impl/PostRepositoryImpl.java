package by.tyv.repository.impl;

import by.tyv.model.entity.Post;
import by.tyv.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Post> findAllPaging(int offset, int amount) {
        return jdbcTemplate.query("SELECT * FROM post ORDER BY id LIMIT ? OFFSET ?", rs -> {
            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                posts.add(mapToPost(rs));
            }
            return posts;
        }, amount, offset);
    }

    @Override
    public List<Post> findAllPaging(String search, int offset, int amount) {
        return jdbcTemplate.query("SELECT * FROM post WHERE tags LIKE ? ORDER BY id LIMIT ? OFFSET ?", rs -> {
            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                posts.add(mapToPost(rs));
            }
            return posts;
        }, "%" + search + "%", amount, offset);
    }


    private Post mapToPost(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getString("image"),
                resultSet.getString("text"),
                null,
                resultSet.getInt("likes_count"),
                null);
    }
}
