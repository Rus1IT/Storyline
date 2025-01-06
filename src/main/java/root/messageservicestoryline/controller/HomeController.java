package root.messageservicestoryline.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Hello, home";
    }

    @GetMapping("/page")
    public String check() {
        return "Hello, home";
    }

    @GetMapping("/secured")
    public String secured(HttpServletRequest request){
        return "Hello, secured ";
    }
}
