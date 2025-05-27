package by.tyv.service.impl;

import by.tyv.config.ContentPaths;
import by.tyv.exception.DataNotFoundException;
import by.tyv.model.entity.Post;
import by.tyv.model.view.Paging;
import by.tyv.model.view.PostPage;
import by.tyv.repository.PostRepository;
import by.tyv.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostRepository repository;
    private final ContentPaths paths;

    @Override
    public PostPage getPostPage(String search, int pageNumber, int pageSize) {
        int offset = pageSize * (pageNumber - 1);
        int findAmount = pageSize + 1;
        List<Post> posts =  Objects.isNull(search)
                ? repository.findAllPaging(offset, findAmount)
                : repository.findAllPaging(search, offset, findAmount);

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

        return new PostPage(paging, posts);
    }

    @Override
    public long createNewPostAndGetId(String title, String text, MultipartFile image, String tags) {
        String fileName = UUID.randomUUID().toString();
        try {
            Files.write(Paths.get(paths.getImagePathStr(), fileName), image.getBytes());
            return repository.saveNewPost(new Post()
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
        return repository.findPostImageNameById(id)
                .map(this::readImageByName)
                .orElseThrow(() -> new DataNotFoundException("Image for post with id %d not found".formatted(id)));
    }

    @Override
    public void likePost(long id, boolean like) {
        if (like) {
            repository.addLike(id);
        } else {
            repository.dislikeLike(id);
        }
    }

    @Override
    public void updatePostById(long id, String title, String text, MultipartFile image, String tags) {
    }

    @Override
    public void addComment(long id, String comment) {

    }

    @Override
    public void editComment(long postId, String comment) {

    }

    @Override
    public void deleteComment(long commentId) {

    }

    @Override
    public void deletePost(long id) {

    }

    @Override
    public Post getPostById(Long id) {
        return repository.findById(id)
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
