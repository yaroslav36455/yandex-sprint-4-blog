package by.tyv.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Long id;
    private String title;
    private String textPreview;
    private List<Comment> comments;
    private int likesCount;
    private List<String> tags;
}
