package by.tyv.controller;

import by.tyv.model.view.PostPage;
import by.tyv.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static by.tyv.util.ControllerUtil.redirect;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    public static final String PAGE_POSTS = "posts";
    public static final String PAGE_POST = "post";
    public static final String PAGE_ADD_POST = "add-post";

    private final PostService postService;

    // б) GET "posts" - список постов на странице ленты постов
    @GetMapping
    public String getPosts(Model model,
                           @RequestParam(value = "search", required = false) String search,
                           @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                           @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        PostPage postPage = postService.getPostPage(search, pageNumber, pageSize);
        model.addAttribute("posts", postPage.getPosts());
        model.addAttribute("paging", postPage.getPaging());
        return PAGE_POSTS;
    }

    // в) GET "/posts/{id}" - страница с постом
    @GetMapping("/{id}")
    public String getPostById(@PathVariable("id") long id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        return PAGE_POST;
    }

    // г) GET "/posts/add" - страница добавления поста
    @GetMapping("/add")
    public String getAddPost() {
        return PAGE_ADD_POST;
    }

    // д) POST "/posts" - добавление поста
    @PostMapping
    public String addPost(@RequestParam("title") String title,
                          @RequestParam("text") String text,
                          @RequestParam("image") MultipartFile image,
                          @RequestParam("tags") String tags) {
        long id = postService.createNewPostAndGetId(title, text, image, tags);
        return redirect("/posts/" + id);
    }

    // е) GET "/images/{id}" -эндпоин, возвращающий набор байт картинки поста
    @GetMapping(value = "/{id}/images", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getImageById(@PathVariable("id") long id) {
        return postService.getImageByPostId(id);
    }

    // ж) POST "/posts/{id}/like" - увеличение/уменьшение числа лайков поста
    @PostMapping("/{id}/like")
    public String likePost(@PathVariable("id") long id,
                           @RequestParam("like") boolean like) {
        postService.likePost(id, like);
        return redirect("/posts/" + id);
    }

    // з) GET "/posts/{id}/edit" - страница редактирования поста
    @GetMapping("/{id}/edit")
    public String editPostPage(Model model, @PathVariable("id") long id) {
        model.addAttribute("post", postService.getPostById(id));
        return PAGE_ADD_POST;
    }

    // и) POST "/posts/{id}" - редактирование поста
    @PostMapping("/{id}")
    public String editPost(@PathVariable("id") long id,
                           @RequestParam("title") String title,
                           @RequestParam("text") String text,
                           @RequestParam("image") MultipartFile image,
                           @RequestParam("tags") String tags) {
        postService.updatePost(id, title, text, image, tags);
        return redirect("/posts/" + id);
    }

    // к) POST "/posts/{id}/comments" - эндпоинт добавления комментария к посту
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable("id") long id,
                             @RequestParam("text") String comment) {
        postService.addComment(id, comment);
        return redirect("/posts/" + id);
    }

    // л) POST "/posts/{id}/comments/{commentId}" - эндпоинт редактирования комментария
    @PostMapping("/{id}/comments/{commentId}")
    public String editComment(@PathVariable("id") long id,
                              @PathVariable("commentId") long commentId,
                              @RequestParam("comment") String comment) {
        postService.editComment(commentId, comment);
        return redirect("/posts/" + id);
    }

    // м) POST "/posts/{id}/comments/{commentId}/delete" - эндпоинт удаления комментария
    @PostMapping("/{id}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("id") long id,
                                @PathVariable("commentId") long commentId) {
        postService.deleteComment(commentId);
        return redirect("/posts/" + id);
    }

    // н) POST "/posts/{id}/delete" - эндпоинт удаления поста
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") long id) {
        postService.deletePost(id);
        return redirect("/posts");
    }
}
