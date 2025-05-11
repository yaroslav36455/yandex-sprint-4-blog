package by.tyv.controller;

import by.tyv.model.view.PostPage;
import by.tyv.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    public static final String PAGE_POSTS = "posts";
    public static final String PAGE_POST = "post";
    public static final String PAGE_ADD_POST = "add-post";

    private final PostService postService;

    // б) GET "posts" - список постов на странице ленты постов
    @GetMapping
    public String getPosts(Model model,
                           @RequestParam(value = "search", required = false) String search,
                           @RequestParam("pageNumber") int pageNumber,
                           @RequestParam("pageSize") int pageSize) {
        PostPage postPage = postService.getPostPage(search, pageNumber, pageSize);
        model.addAttribute("posts", postPage.getPosts());
        model.addAttribute("paging", postPage.getPaging());
        return PAGE_POSTS;
    }

    // в) GET "/posts/{id}" - страница с постом
    @GetMapping("/{id}")
    public String getPostById(@PathVariable("id") int id, Model model) {
        model.addAttribute("posts", postService.getPostById(id));
        return PAGE_POST;
    }

    // г) GET "/posts/add" - страница добавления поста
    @GetMapping("/add")
    public String getAddPost() {
        return PAGE_ADD_POST;
    }

    // д) POST "/posts" - добавление поста
    @PostMapping
    public String addPost() {

        return "redirect:/posts/{id}";
    }

    // ж) POST "/posts/{id}/like" - увеличение/уменьшение числа лайков поста
    @PostMapping("/{id}/like")
    public String likePost(@PathVariable("id") int id,
                           @RequestParam("like") boolean isLike) {

        return "redirect:/posts/{id}";
    }

    // з) POST "/posts/{id}/edit" - страница редактирования поста
    @PostMapping("/{id}/edit")
    public String editPostPage(@PathVariable("id") int id) {
        return PAGE_ADD_POST;
    }

    // и) POST "/posts/{id}" - редактирование поста
    @PostMapping("/{id}")
    public String editPost(@PathVariable("id") int id, MultipartFile file) {
        return "redirect:/posts/{id}";
    }

    // к) POST "/posts/{id}/comments" - эндпоинт добавления комментария к посту
    @PostMapping("/{id}/comments")
    public String addComment(@PathVariable("id") int id,
                             @RequestBody String comment) {
        return "redirect:/posts/{id}";
    }

    // л) POST "/posts/{id}/comments/{commentId}" - эндпоинт редактирования комментария
    @PostMapping("/{id}/comments/{commentId}")
    public String editComment(@PathVariable("id") int id,
                              @PathVariable("commentId") int commentId,
                              @RequestBody String comment) {
        return "redirect:/posts/{id}";
    }

    // м) POST "/posts/{id}/comments/{commentId}/delete" - эндпоинт удаления комментария
    @PostMapping("/{id}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable("id") int id,
                                @PathVariable("commentId") int commentId) {
        return "redirect:/posts/{id}";
    }

    // н) POST "/posts/{id}/delete" - эндпоинт удаления поста
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") int id) {
        return "redirect:/posts";
    }
}
