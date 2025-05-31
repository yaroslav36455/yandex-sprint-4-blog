package by.tyv.model.view;

import by.tyv.model.bo.Post;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class PostPage {
    private Paging paging;
    private List<Post> posts;
}
