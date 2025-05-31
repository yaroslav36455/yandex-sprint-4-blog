package by.tyv.service;

import by.tyv.config.ContentPaths;
import by.tyv.config.DataSourceConfiguration;
import by.tyv.exception.DataNotFoundException;
import by.tyv.model.bo.Comment;
import by.tyv.model.bo.Post;
import by.tyv.model.view.PostPage;
import by.tyv.repository.CommentRepository;
import by.tyv.repository.PostRepository;
import by.tyv.repository.impl.CommentRepositoryImpl;
import by.tyv.repository.impl.PostRepositoryImpl;
import by.tyv.service.impl.PostServiceImpl;
import by.tyv.util.DataUtil;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(classes = {DataSourceConfiguration.class, PostServiceImpl.class,
        PostRepositoryImpl.class, CommentRepositoryImpl.class})
@EnableTransactionManagement
@Sql(scripts = "/sql/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @MockitoBean
    private ContentPaths paths;

    @BeforeEach
    public void beforeEach() throws URISyntaxException {
        Mockito.doReturn(Paths.get(getClass().getClassLoader().getResource("images").toURI()).toString())
                .when(paths)
                .getImagePathStr();
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Получить все посты страницы")
    public void getAllPostsPagedFirstPage() {
        PostPage postPage = postService.getPostPage(null, 1, 3);

        Assertions.assertThat(postPage.getPosts().size()).isEqualTo(3);
        Assertions.assertThat(postPage.getPaging().getPageNumber()).isEqualTo(1);
        Assertions.assertThat(postPage.getPaging().getPageSize()).isEqualTo(3);
        Assertions.assertThat(postPage.getPaging().hasPrevious()).isFalse();
        Assertions.assertThat(postPage.getPaging().hasNext()).isTrue();
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Получить все оставшиеся посты страницы")
    public void getAllPostsPagedLastPage() {
        PostPage postPage = postService.getPostPage(null, 2, 5);

        Assertions.assertThat(postPage.getPosts().size()).isEqualTo(3);
        Assertions.assertThat(postPage.getPaging().getPageNumber()).isEqualTo(2);
        Assertions.assertThat(postPage.getPaging().getPageSize()).isEqualTo(5);
        Assertions.assertThat(postPage.getPaging().hasPrevious()).isTrue();
        Assertions.assertThat(postPage.getPaging().hasNext()).isFalse();
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Получить все оставшиеся посты страницы, размер страницы больше, чем количество постов всего")
    public void getAllPostsPagedFirstAndLastPage() {
        PostPage postPage = postService.getPostPage(null, 1, 10);

        Assertions.assertThat(postPage.getPosts().size()).isEqualTo(8);
        Assertions.assertThat(postPage.getPaging().getPageNumber()).isEqualTo(1);
        Assertions.assertThat(postPage.getPaging().getPageSize()).isEqualTo(10);
        Assertions.assertThat(postPage.getPaging().hasPrevious()).isFalse();
        Assertions.assertThat(postPage.getPaging().hasNext()).isFalse();
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Найти посты по тегу, всего 3 поста, но читается вторая страница размером 2, а значит в ответе будет один пост")
    public void getAllPostsByTagPagedFirstAndLastPage() {
        String tag = "tag3";
        PostPage postPage = postService.getPostPage(tag, 2, 2);

        Assertions.assertThat(postPage.getPosts().size()).isEqualTo(1);
        Assertions.assertThat(postPage.getPaging().getPageNumber()).isEqualTo(2);
        Assertions.assertThat(postPage.getPaging().getPageSize()).isEqualTo(2);
        Assertions.assertThat(postPage.getPaging().hasPrevious()).isTrue();
        Assertions.assertThat(postPage.getPaging().hasNext()).isFalse();
        Assertions.assertThat(postPage.getPosts().getFirst())
                .extracting(Post::getTitle)
                .isEqualTo("Title-7");
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Найти посты по тегу, всего 3 поста, но читается вторая страница размером 2, а значит в ответе будет один пост")
    public void findPostById() {
        Post post = postService.getPostById(1L);

        Assertions.assertThat(post)
                .usingRecursiveComparison()
                .isEqualTo(DataUtil.getPost1());
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Найти пост по идентификатору, которого нет в базе, должен выбросить исключение DataNotFoundException")
    public void findPostByIdNotFound() {
        Assertions.assertThatThrownBy(() -> postService.getPostById(999L))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Найти пост по идентификатору, вернуть массив байтов изображения")
    public void findPostImageByPostId() {
        byte[] image = postService.getImageByPostId(3L);

        Assertions.assertThat(image)
                .isNotNull();
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Найти изображение по id поста которого нет в базе, выбросить исключение DataNotFoundException")
    public void findPostImageByPostIdNotFound() {
        Assertions.assertThatThrownBy(() -> postService.getImageByPostId(999L))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert_post_with_incorrect_image_name.sql"})
    @DisplayName("Найти изображение по id поста которого нет в базе, выбросить исключение DataNotFoundException")
    public void findPostImageByPostIdAndImageNotFound() {
        Assertions.assertThatThrownBy(() -> postService.getImageByPostId(1L))
                .isInstanceOf(DataNotFoundException.class);
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql"})
    @DisplayName("Создать новый пост и вернуть его идентификатор")
    public void createNewPostAndGetId() {
        MockMultipartFile multipartFile = new MockMultipartFile("image", "image.png", "text/plain", "Байты изображения".getBytes());

        postService.createNewPostAndGetId("NewSavedPostTitle", "Text", multipartFile, "tag1 tag2");
        Optional<Post> foundPost = postRepository.findById(1L);

        Assertions.assertThat(foundPost)
                .isPresent()
                .get()
                .extracting(Post::getTitle)
                .isEqualTo("NewSavedPostTitle");
    }

    @Test
    @DisplayName("У поста один лайк, добавить ещё один лайк к посту")
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    public void addLikeToPost() {
        postService.likePost(1L, true);

        Assertions.assertThat(postRepository.findById(1L))
                .isPresent()
                .get()
                .extracting(Post::getLikesCount)
                .isEqualTo(2);
    }

    @Test
    @DisplayName("У поста один лайк, уменьшить количество лайков на 1")
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    public void dislikePost() {
        postService.likePost(1L, false);

        Assertions.assertThat(postRepository.findById(1L))
                .isPresent()
                .get()
                .extracting(Post::getLikesCount)
                .isEqualTo(0);
    }

    @Test
    @DisplayName("У поста ни одного лайка, при попытке уменьшения количества лайков на 1 ничего не произойдёт")
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert_post_without_likes.sql"})
    public void tryDislikePostWithoutLikes() {
        postService.likePost(1L, false);

        Assertions.assertThat(postRepository.findById(1L))
                .isPresent()
                .get()
                .extracting(Post::getLikesCount)
                .isEqualTo(0);
    }

    @Test
    @DisplayName("Обновить существующий пост")
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    public void updatePost() {
        MockMultipartFile multipartFile = new MockMultipartFile("image", "image.txt",
                MediaType.TEXT_PLAIN_VALUE, "Байты изображения".getBytes());

        postService.updatePost(1L, "NewTitle", "NewText", multipartFile, "tag1 tag2");
        Assertions.assertThat(postRepository.findById(1L))
                .isPresent()
                .get()
                .extracting(Post::getTitle, Post::getText, Post::getTags)
                .containsExactly("NewTitle", "NewText", List.of("tag1", "tag2"));
    }

    @Test
    @DisplayName("Удалить пост по id")
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    public void deletePost() {
        postService.deletePost(2L);
        Assertions.assertThat(postRepository.findById(2L))
                .isNotPresent();
    }

    @Test
    @DisplayName("Добавить комментарий к посту")
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    public void addCommentToPost() {
        final long postId = 1L;
        final String commentText = "New test comment";
        postService.addComment(postId, commentText);

        Assertions.assertThat(commentRepository.findCommentsByPostId(postId))
                .usingRecursiveFieldByFieldElementComparator(RecursiveComparisonConfiguration.builder()
                        .withIgnoredFields("id")
                        .build())
                .contains(new Comment(null, commentText, postId));
    }

    @Test
    @DisplayName("Редактирование комментария поста")
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    public void editCommentToPost() {
        final long commentId = 1L;
        final String newCommentText = "Edited test comment";
        postService.editComment(commentId, newCommentText);

        Optional<Comment> actualComment = commentRepository.findById(commentId);
        Assertions.assertThat(actualComment).isPresent();
        Assertions.assertThat(actualComment.get())
                .usingRecursiveComparison(RecursiveComparisonConfiguration.builder()
                        .withIgnoredFields("postId")
                        .build())
                .isEqualTo(new Comment(commentId, newCommentText, null));
    }

    @Test
    @DisplayName("Удаление комментария")
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    public void deleteCommentToPost() {
        final long commentId = 1L;
        postService.deleteComment(commentId);

        Optional<Comment> actualComment = commentRepository.findById(commentId);
        Assertions.assertThat(actualComment).isNotPresent();;
    }
}
