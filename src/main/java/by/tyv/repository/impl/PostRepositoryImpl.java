package by.tyv.repository.impl;

import by.tyv.model.bo.Post;
import by.tyv.repository.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public List<Post> findAllPaging(int offset, int amount) {
        return jdbcTemplate.query("SELECT p.id, p.title, p.image, p.text, p.likes_count, p.tags FROM post p ORDER BY id LIMIT ? OFFSET ?", rs -> {
            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                posts.add(mapToPost(rs));
            }
            return posts;
        }, amount, offset);
    }

    @Override
    public List<Post> findAllPaging(String search, int offset, int amount) {
        return jdbcTemplate.query("SELECT p.id, p.title, p.image, p.text, p.likes_count, p.tags FROM post p WHERE tags LIKE ? ORDER BY id LIMIT ? OFFSET ?", rs -> {
            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                posts.add(mapToPost(rs));
            }
            return posts;
        }, "%" + search + "%", amount, offset);
    }

    @Override
    public Optional<Post> findById(Long id) {
        final String sql = "SELECT p.id, p.title, p.image, p.text, p.likes_count, p.tags FROM post p WHERE p.id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                    (rs, rowNum) -> mapToPost(rs), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> findPostImageNameById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT image FROM post WHERE id = ?",
                    (rs, rowNum) -> rs.getString("image"), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public long save(Post post) throws JsonProcessingException {
        return Objects.isNull(post.getId())
                ? saveNewPost(post)
                : updatePost(post);
    }

    private long saveNewPost(Post newPost) throws JsonProcessingException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String tagsString = jsonMapper.writeValueAsString(newPost.getTags());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO post (title, text, image, likes_count, tags) VALUES (?, ?, ?, ?, ?)",
                    new String[] { "id" }
            );
            ps.setString(1, newPost.getTitle());
            ps.setString(2, newPost.getText());
            ps.setString(3, newPost.getImage());
            ps.setInt(4, newPost.getLikesCount());
            ps.setString(5, tagsString);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private long updatePost(Post post) throws JsonProcessingException {
        String tagsString = jsonMapper.writeValueAsString(post.getTags());
        jdbcTemplate.update("UPDATE post SET title = ?, text = ?, image = ?, tags = ? WHERE id = ?",
                post.getTitle(), post.getText(), post.getImage(), tagsString, post.getId());

        return post.getId();
    }

    @Override
    public void addLike(long id) {
        jdbcTemplate.update("UPDATE post SET likes_count = likes_count + 1 WHERE id = ?", id);
    }

    @Override
    public void dislikeLike(long id) {
        jdbcTemplate.update("UPDATE post SET likes_count = case when likes_count > 0 then likes_count - 1 else 0 end WHERE id = ?", id);
    }

    @Override
    public void deleteById(long id) {
        jdbcTemplate.update("DELETE FROM post WHERE id = ?", id);
    }

    private Post mapToPost(ResultSet resultSet) {

        try {
            String tagsDeserialized = jsonMapper.readValue(resultSet.getString("tags"), String.class);
            List<String> tagList = jsonMapper.readValue(tagsDeserialized, new TypeReference<List<String>>() {});

            return new Post(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("image"),
                    resultSet.getString("text"),
                    new ArrayList<>(),
                    resultSet.getInt("likes_count"),
                    tagList);
        } catch (SQLException e) {
            throw new RuntimeException("Unexpected SQL Exception", e);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Unexpected Jackson mapping Exception", e);
        }
    }
}
