package root.messageservicestoryline.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import root.messageservicestoryline.service.UserManagementService;

@RestController
public class HomeController {

    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("/home")
    public String home() {
        return "Hello, home";
    }

    @GetMapping("user/home")
    public String check() {
        return "Hello, home";
    }

    @GetMapping("admin/home")
    public String secured(HttpServletRequest request){
        return "Hello, secured ";
    }
}
