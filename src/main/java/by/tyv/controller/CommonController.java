package by.tyv.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static by.tyv.util.ControllerUtil.redirect;

@Controller
public class CommonController {

    // а) GET "/" - редирект на "/posts"
    @GetMapping
    public String redirectToPosts() {
        return redirect("/posts");
    }
}
