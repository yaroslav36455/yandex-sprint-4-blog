package by.tyv.controller;

import by.tyv.config.WebConfiguration;
import by.tyv.model.entity.Comment;
import by.tyv.model.entity.Post;
import by.tyv.model.view.Paging;
import by.tyv.model.view.PostPage;
import by.tyv.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(classes = WebConfiguration.class)
@WebAppConfiguration
public class PostControllerTest {

    @MockitoBean
    private PostService postService;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    @DisplayName("Вернуть список постов на странице ленты постов, статус 200")
    public void getPosts() throws Exception {
        int pageNumber = 1;
        int pageSize = 10;
        List<Comment> firstPostComments = List.of(new Comment(1L, "commentText"), new Comment(2L, "commentText 2"));
        PostPage mockPostPage = new PostPage(new Paging(1, 2, false, false),
                List.of(
                        new Post(1L, "Title", "Text", firstPostComments, 5, List.of("tag1", "tag2")),
                        new Post(2L, "Title2", "Text2", List.of(), 0, List.of("tag2", "tag3"))
                ));

        Mockito.doReturn(mockPostPage)
                .when(postService)
                .getPostPage(null, pageNumber, pageSize);

        mockMvc.perform(get("/posts")
                        .param("pageNumber", String.valueOf(pageNumber))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attribute("paging", mockPostPage.getPaging()))
                .andExpect(model().attribute("posts", mockPostPage.getPosts()));
    }
}