package com.pyro.controllers;


import com.pyro.entities.Basket;
import com.pyro.entities.Deal;
import com.pyro.entities.Message;
import com.pyro.entities.User;
import com.pyro.repositories.*;
import com.pyro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;

@Controller
public class PrivateController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CatRepository catRepository;

    @Autowired
    UserService userService;

    @Autowired
    DealRepository dealRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    BasketRepository basketRepository;

    @Transactional
    @PostMapping("/buy")
    public String getProductList(@RequestParam(value = "basketid", required = true) Long basketId, Principal principal) {
        Basket basket = basketRepository.getOne(basketId);
        User user = userService.findByUsername(principal.getName());
        for (Deal product : basket.getDeals()) {
            Deal deal = new Deal();
            deal.setUser(user);
            deal.setDate(new Date());
            deal.setProduct(product.getProduct());
            deal.setBought(1);
            dealRepository.saveAndFlush(deal);
            Message message = new Message(user, "Вы сделали заказ",
                    "Вы сделали заказ на " + deal.getProduct().getName() + ". Курьер свяжется с вами в ближайшее время.", new Date());
            messageRepository.saveAndFlush(message);
        }
        basket.clearBasket();
        dealRepository.deleteByBoughtAndUser(0, user);
        basketRepository.saveAndFlush(basket);
        return "redirect:/private";
    }

    @PostMapping("/removefrombasket")
    public String rem(@RequestParam(value = "basketid") Long basketId,
                      @RequestParam(value = "dealid") Long dealid) {
        Basket basket = basketRepository.findById(basketId).get();
        basket.getDeals().removeIf(n -> n.getId() == dealid);
        basketRepository.saveAndFlush(basket);
        return "redirect:/private";
    }

}