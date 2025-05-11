package by.tyv.service;

import by.tyv.model.entity.Post;
import by.tyv.model.view.PostPage;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    Post getPostById(Long id);

    PostPage getPostPage(String search, int pageNumber, int pageSize);

    long createNewPostAndGetId(String title, String text, MultipartFile image, String tags);

    byte[] getImageByPostId(long id);
}
