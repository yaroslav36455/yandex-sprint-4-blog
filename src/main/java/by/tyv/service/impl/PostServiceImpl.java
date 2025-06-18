package by.tyv.service.impl;

import by.tyv.config.ContentPaths;
import by.tyv.exception.DataNotFoundException;
import by.tyv.model.bo.Post;
import by.tyv.model.view.Paging;
import by.tyv.model.view.PostPage;
import by.tyv.repository.CommentRepository;
import by.tyv.repository.PostRepository;
import by.tyv.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ContentPaths paths;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public PostPage getPostPage(String search, int pageNumber, int pageSize) {
        int offset = pageSize * (pageNumber - 1);
        int findAmount = pageSize + 1;
        List<Post> posts = Objects.isNull(search)
                ? postRepository.findAllPaging(offset, findAmount)
                : postRepository.findAllPaging(search, offset, findAmount);

        Paging paging = new Paging();
        paging.setPageNumber(pageNumber);
        paging.setPageSize(pageSize);
        paging.setHasPrevious(pageNumber > 1);

        if (findAmount == posts.size()) {
            paging.setHasNext(true);
            posts = posts.subList(0, pageSize);
        } else {
            paging.setHasNext(false);
        }

        Map<Long, Post> postMap = posts.stream().collect(Collectors.toMap(Post::getId, post -> post));
        commentRepository.findCommentsByPostId(posts.stream().map(Post::getId).toList())
                .forEach(comment -> postMap.get(comment.getPostId()).getComments().add(comment));

        return new PostPage(paging, posts);
    }

    @Override
    public long createNewPostAndGetId(String title, String text, MultipartFile image, String tags) {
        String fileName = UUID.randomUUID().toString();
        try {
            Files.write(Paths.get(paths.getImagePathStr(), fileName), image.getBytes());
            return postRepository.save(new Post()
                    .setTitle(title)
                    .setText(text)
                    .setImage(fileName)
                    .setTags(Arrays.asList(tags.split("[ ,]+", 10))));
        } catch (IOException e) {
            log.error("Error while saving image: {}", fileName, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getImageByPostId(long id) {
        return postRepository.findPostImageNameById(id)
                .map(this::readImageByName)
                .orElseThrow(() -> new DataNotFoundException("Image for post with id %d not found".formatted(id)));
    }

    @Override
    public void likePost(long id, boolean like) {
        if (like) {
            postRepository.addLike(id);
        } else {
            postRepository.dislikeLike(id);
        }
    }

    @Override
    public void updatePost(long id, String title, String text, MultipartFile image, String tags) {
        Optional<Post> foundPost = postRepository.findById(id);

        if (foundPost.isPresent()) {
            Post post = foundPost.get();

            post.setTitle(title);
            post.setText(text);
            post.setTags(Arrays.asList(tags.split("[ ,]+", 10)));

            try {
                if (image.isEmpty()) {
                    postRepository.save(post);
                } else {
                    String oldFileName = post.getImage();
                    String newFileName = UUID.randomUUID().toString();
                    post.setImage(newFileName);

                    Files.write(Paths.get(paths.getImagePathStr(), newFileName), image.getBytes());
                    postRepository.save(post);
                    Files.deleteIfExists(Paths.get(paths.getImagePathStr(), oldFileName));

                }
            } catch (IOException e) {
                log.error("Error while saving post: {}", post, e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void addComment(long postId, String comment) {
        commentRepository.addCommentByPostId(postId, comment);
    }

    @Override
    public void editComment(long commentId, String comment) {
        jdbcTemplate.update("UPDATE comment SET text = ? WHERE id = ?", comment, commentId);
    }

    @Override
    public void deleteComment(long commentId) {
        jdbcTemplate.update("DELETE FROM comment WHERE id = ?", commentId);
    }

    @Override
    @Transactional
    public void deletePost(long id) {
        Optional<String> imageName = postRepository.findPostImageNameById(id);
        commentRepository.deleteByPostId(id);
        postRepository.deleteById(id);

        imageName.ifPresent(fileName -> {
            try {
                Files.deleteIfExists(Paths.get(paths.getImagePathStr(), fileName));
            } catch (IOException e) {
                log.error("Error while deleting image: {}", fileName, e);
                throw new RuntimeException(e);
            }
            log.info("Image {} deleted", fileName);
        });
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setComments(commentRepository.findCommentsByPostId(id));
                    return post;
                })
                .orElseThrow(() -> new DataNotFoundException("Post with id %d not found".formatted(id)));
    }

    private byte[] readImageByName(String imageName) {
        try {
            return Files.readAllBytes(Paths.get(paths.getImagePathStr(), imageName));
        } catch (IOException e) {
            log.warn("Error while reading image by name: {}", imageName, e);
            return null;
        }
    }
}
