package app.controller;

import app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles routing for protected user dashboard.
 * Access is restricted to authenticated users via SecurityConfig.
 */
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String viewUserList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
}