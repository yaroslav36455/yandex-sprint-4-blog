package by.tyv.service.impl;

import by.tyv.exception.DataNotFoundException;
import by.tyv.model.entity.Post;
import by.tyv.model.view.Paging;
import by.tyv.model.view.PostPage;
import by.tyv.repository.PostRepository;
import by.tyv.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository repository;

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
        return 0;
    }

    @Override
    public byte[] getImageByPostId(long id) {
        return new byte[0];
    }

    @Override
    public void likePost(long id, boolean like) {

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
}
