package by.tyv.model.entity;


import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
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
