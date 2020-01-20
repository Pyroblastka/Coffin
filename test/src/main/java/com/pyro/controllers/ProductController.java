package com.pyro.controllers;


import com.pyro.entities.*;
import com.pyro.repositories.BasketRepository;
import com.pyro.repositories.CatRepository;
import com.pyro.repositories.DealRepository;
import com.pyro.repositories.ProductRepository;
import com.pyro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CatRepository catRepository;

    @Autowired
    UserService userService;

    @Autowired
    BasketRepository basketRepository;

    @Autowired
    DealRepository dealRepository;

    @GetMapping("/product")
    public String getProduct(@RequestParam(value = "productId", required = true) Long productId,
                             @RequestParam(value = "bought", required = false) String bought,
                             Model model) {

        Product product = productRepository.getOne(productId);
        model.addAttribute("product", product);

        return "/product";
    }

    @GetMapping("/productq")
    public String getProductByName(@RequestParam(value = "productName", required = true) String productName,
                             Model model) {
        if(productName==null||productName=="") {
            return "redirect:/index";
        }
        Product product = productRepository.findByName(productName);
        if(product == null)
        {
            Category category = null;
            List<Category> categories = catRepository.findByNameContainsIgnoreCase(productName);
            if(categories.size()>0)
             category = categories.get(0);
            if(category == null)
            {
                return "redirect:/index";
            }
            else {
                return "redirect:/products?catId="+category.getId();
            }
        }
        model.addAttribute("product", product);
        return "/product";
    }

    @GetMapping("/products")
    public String getProductList(@RequestParam(value = "catId", required = true) Long catId,
                                 Model model) {
        List<Product> list = productRepository.findByCategory(catRepository.getOne(catId));
        model.addAttribute("products", list);

        return "/products";
    }

    @PostMapping("/addtobasket")
    public String getProductList(@RequestParam(value = "id", required = true) Long productId,
                                 Model model,
                                 Principal principal) {

        User user = userService.findByUsername(principal.getName());

        Basket basket = basketRepository.findByUser(user);
        if (basket == null) {
            basket = new Basket(user, new ArrayList<Deal>());
        }

        Deal deal = new Deal(user, productRepository.getOne(productId), new Date(), 0);
        dealRepository.saveAndFlush(deal);
        basket.getDeals().add(deal);
        basketRepository.saveAndFlush(basket);
        Product product = productRepository.getOne(productId);
        model.addAttribute("product", product);
        model.addAttribute("bought", 1);
        return "/product";
    }

}