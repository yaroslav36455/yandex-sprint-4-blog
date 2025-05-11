package by.tyv.service.impl;

import by.tyv.model.entity.Post;
import by.tyv.model.view.PostPage;
import by.tyv.repository.PostRepository;
import by.tyv.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository repository;

    @Override
    @Transactional
    public PostPage getPostPage(String search, int pageNumber, int pageSize) {
        return null;
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
    public Post getPostById(Long id) {
        return null;
    }
}
