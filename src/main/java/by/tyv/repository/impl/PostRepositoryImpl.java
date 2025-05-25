package by.tyv.repository.impl;

import by.tyv.model.entity.Post;
import by.tyv.repository.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper jsonMapper = new ObjectMapper();

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

    @Override
    public Optional<Post> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM post WHERE id = ?",
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


    private Post mapToPost(ResultSet resultSet) {

        try {
            String tagsDeserialized = jsonMapper.readValue(resultSet.getString("tags"), String.class);
            List<String> tagList = jsonMapper.readValue(tagsDeserialized, new TypeReference<List<String>>() {});

            return new Post(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("image"),
                    resultSet.getString("text"),
                    List.of(),
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
