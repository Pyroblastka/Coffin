package com.pyro.controllers;


import com.pyro.entities.Basket;
import com.pyro.entities.Deal;
import com.pyro.entities.Message;
import com.pyro.entities.User;
import com.pyro.repositories.BasketRepository;
import com.pyro.repositories.DealRepository;
import com.pyro.repositories.MessageRepository;
import com.pyro.repositories.RoleRepository;
import com.pyro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserService userService;

    @Autowired
    DealRepository dealRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    BasketRepository basketRepository;

    @GetMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/private")
    public String privateCab(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Basket basket = basketRepository.findByUser(user);
        if(basket==null)
        {
            basket = new Basket(user);
            basketRepository.saveAndFlush(basket) ;
        }
        model.addAttribute("basket", basket);
        List<Deal> list = dealRepository.findByUser(user);
        model.addAttribute("deals", list);
        List<Message> messages = messageRepository.findByUser(user);
        Collections.sort(messages, Message::compareTo);
        model.addAttribute("messages", messages);
        model.addAttribute("total", basket.getTotalCost());
        return "private";
    }

    @GetMapping("/registration")
    public String reg(Model model) {

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          Model model) {


        if (userService.findByUsername(username) != null) {
            model.addAttribute("error", "Пользователь "+username+" уже зарегистрирован");
            return "/registration";
        }
        if (!(username.matches("^[a-zA-Z0-9]+$"))) {
            model.addAttribute("error", "Имя пользователя может содержать только латиницу и цифры");
            return "/registration";
        }
        User user = new User(1, username, password, Arrays.asList(roleRepository.findByName("ROLE_USER")));
        userService.save(user);
        return "/index";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/about")
    public String about() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/403")
    public String error403() {
        return "error/403";
    }
}
