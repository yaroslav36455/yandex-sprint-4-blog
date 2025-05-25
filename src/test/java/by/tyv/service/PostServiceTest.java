package by.tyv.service;

import by.tyv.config.DataSourceConfiguration;
import by.tyv.exception.DataNotFoundException;
import by.tyv.model.entity.Post;
import by.tyv.model.view.PostPage;
import by.tyv.repository.impl.PostRepositoryImpl;
import by.tyv.service.impl.PostServiceImpl;
import by.tyv.util.DataUtil;
import jakarta.servlet.ServletContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(classes = {DataSourceConfiguration.class, PostServiceImpl.class, PostRepositoryImpl.class})
@EnableTransactionManagement
@Sql(scripts = "/sql/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class PostServiceTest {
    @Autowired
    private PostService postService;
    @MockitoBean
    private ServletContext servletContext;

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
        PostPage postPage = postService.getPostPage(null, 2, 4);

        Assertions.assertThat(postPage.getPosts().size()).isEqualTo(3);
        Assertions.assertThat(postPage.getPaging().getPageNumber()).isEqualTo(2);
        Assertions.assertThat(postPage.getPaging().getPageSize()).isEqualTo(4);
        Assertions.assertThat(postPage.getPaging().hasPrevious()).isTrue();
        Assertions.assertThat(postPage.getPaging().hasNext()).isFalse();
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Получить все оставшиеся посты страницы, размер страницы больше, чем количество постов всего")
    public void getAllPostsPagedFirstAndLastPage() {
        PostPage postPage = postService.getPostPage(null, 1, 10);

        Assertions.assertThat(postPage.getPosts().size()).isEqualTo(7);
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
                .isEqualTo("Title-6");
    }

    @Test
    @Sql(scripts = {"/sql/clear.sql", "/sql/insert.sql"})
    @DisplayName("Найти посты по тегу, всего 3 поста, но читается вторая страница размером 2, а значит в ответе будет один пост")
    public void findPostById() {
        Post post = postService.getPostById(1L);

        Assertions.assertThat(post)
                .usingRecursiveAssertion()
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
        byte[] image = postService.getImageByPostId(1L);

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
}
