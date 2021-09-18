package ru.jm.spring_mvc_hibernate.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.jm.spring_mvc_hibernate.entity.User;
import ru.jm.spring_mvc_hibernate.service.UserService;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/user")
    public String getUserPage(Model model) {
        model.addAttribute("user", getUserData());
        return "user";
    }

    @GetMapping(value = "/login")
    public String login(Model model) {
        model.addAttribute("currentUser", getUserData());
        return "login";
    }

    @GetMapping(value = "/")
    public String getLoginPage() {
        return "redirect:login";
    }

    @GetMapping("/admin")
    public String index(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "index";
    }

    @PostMapping("/admin/adduser")
    public String addUser(@RequestParam(name = "name", defaultValue = "-") String name,
                          @RequestParam(name = "surname", defaultValue = "-") String surname,
                          @RequestParam(name = "age", defaultValue = "0") int age,
                          @RequestParam(name = "email", defaultValue = "-") String email,
                          @RequestParam(name = "username") String username,
                          @RequestParam(name = "password") String password) {
        userService.saveUser(name, surname, age, email, username, password);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String details(Model model, @PathVariable(name = "id") int id) {
        User user = userService.getUser(id);
        if (user == null) {
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping("/admin/saveuser")
    public String saveUser(@RequestParam(name = "id") int id,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "surname") String surname,
                           @RequestParam(name = "age") int age,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "username") String username,
                           @RequestParam(name = "password") String password) {

        User user = userService.getUser(id);

        if (user != null) {
            user.setName(name);
            user.setSurname(surname);
            user.setAge(age);
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(password);
            userService.updateUser(user);
        }

        return "redirect:/admin";
    }

    @DeleteMapping("/admin/deleteuser/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping(value = "/error_access")
    public String accessDenied(Model model) {
        model.addAttribute("user", getUserData());
        return "error_access";
    }

    private User getUserData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User sUser = (User) authentication.getPrincipal();
            return userService.findByUsername(sUser.getUsername());
        }

        return null;
    }
}
