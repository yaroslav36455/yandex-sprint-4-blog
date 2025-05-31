package by.tyv.unit;


import by.tyv.model.bo.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PostTest {

    @BeforeEach
    public void beforeEach() {
        Post post = new Post();
        post.setText("""
                Первое предложение поста. Второе предложение поста! Третье предложение поста? Чётвортое предложение поста!
                Пятое предлоежение поста нового абзаца.""");
        post.setTags(List.of("tag1", "tag2", "tag3"));
    }

    @Test
    @DisplayName("Разбиение текста на абзацы")
    public void getParagraphsTest() {
        Post post = new Post()
                .setText("""
                Первое предложение поста. Второе предложение поста! Третье предложение поста? Чётвортое предложение поста!
                Пятое предлоежение поста нового абзаца.""");

        Assertions.assertThat(post.getParagraphs())
                .hasSize(2)
                .containsExactly(
                        "Первое предложение поста. Второе предложение поста! Третье предложение поста? Чётвортое предложение поста!",
                        "Пятое предлоежение поста нового абзаца.");
    }

    @Test
    @DisplayName("Чтение превью поста - лишь первый абзац, не более трёх строк, изначально в первом абзаце четыре")
    public void getTextPreviewFourLinesTest() {
        Post post = new Post()
                .setText("""
                Первое предложение поста. Второе предложение поста! Третье предложение поста? Чётвортое предложение поста!
                Пятое предлоежение поста нового абзаца.""");
        Assertions.assertThat(post.getTextPreview())
                .isEqualTo("Первое предложение поста. Второе предложение поста! Третье предложение поста?");
    }

    @Test
    @DisplayName("Чтение превью поста - лишь первый абзац, не более трёх строк, изначально в первом абзаце два предложения")
    public void getTextPreviewTwoLinesTest() {
        Post post = new Post()
                .setText("""
                Первое предложение поста. Второе предложение поста!
                Третье предложение поста? Чётвортое предложение поста! Пятое предлоежение поста.""");
        Assertions.assertThat(post.getTextPreview())
                .isEqualTo("Первое предложение поста. Второе предложение поста!");
    }

    @Test
    @DisplayName("Конвертация тегов в строку")
    public void getTagsAsTextTest() {
        Post post = new Post()
                .setTags(List.of("tag1", "tag2", "tag3"));
        Assertions.assertThat(post.getTagsAsText())
                .isEqualTo("tag1, tag2, tag3");
    }
}
