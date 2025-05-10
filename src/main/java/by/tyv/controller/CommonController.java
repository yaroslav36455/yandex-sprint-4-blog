package by.tyv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CommonController {

    // а) GET "/" - редирект на "/posts"
    @GetMapping
    public String redirectToPost() {
        return "redirect:posts";
    }

    // е) GET "/images/{id}" -эндпоин, возвращающий набор байт картинки поста
    @GetMapping("/images/{id}")
    public byte[] getImageById(@PathVariable("id") int id) {
        return null;
    }
}
