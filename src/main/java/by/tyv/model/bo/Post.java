package by.tyv.model.bo;


import lombok.*;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@ToString
public class Post {
    static private final Pattern ONE_PARAGRAPH_THREE_STRING = Pattern.compile("^([^.!?]*[.!?]){1,3}");

    private Long id;
    private String title;
    private String image;
    private String text;
    private List<Comment> comments;
    private int likesCount;
    private List<String> tags;


    public List<String> getParagraphs() {
        return Arrays.asList(text.split("\\v+"));
    }

    public String getTextPreview() {
        Matcher matcher = ONE_PARAGRAPH_THREE_STRING.matcher(getParagraphs().getFirst());

        return matcher.find()
                ? matcher.group().trim()
                : text;
    }

    public String getTagsAsText() {
        return String.join(", ", tags);
    }
}
