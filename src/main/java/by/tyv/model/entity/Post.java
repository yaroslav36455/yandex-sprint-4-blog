package by.tyv.model.entity;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Post {
    private Long id;
    private String title;
    private String image;
    private String text;
    private List<Comment> comments;
    private int likesCount;
    private List<String> tags;


    public List<String> getTextParts() {
        return List.of(text);
    }

    public String getTextPreview() {
        return text;
    }

    public String getTagsAsText() {
        return String.join(", ", tags);
    }
}
