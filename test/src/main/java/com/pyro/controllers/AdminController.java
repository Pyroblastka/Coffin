package com.pyro.controllers;


import com.pyro.entities.Deal;
import com.pyro.entities.Message;
import com.pyro.entities.Product;
import com.pyro.entities.User;
import com.pyro.repositories.CatRepository;
import com.pyro.repositories.DealRepository;
import com.pyro.repositories.MessageRepository;
import com.pyro.repositories.ProductRepository;
import com.pyro.service.DBFileStorageService;
import com.pyro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private DBFileStorageService imageService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CatRepository catRepository;

    @Autowired
    DBFileStorageService fileStorageService;

    @Autowired
    UserService userService;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    DealRepository dealRepository;

    @GetMapping("/vr")
    public String admin() {
        return "vr";
    }


    @GetMapping("/admin")
    public String admin(Model model) {
        List<Deal> deals =  dealRepository.findAll();
        Collections.sort(deals, Deal::compareTo);
        model.addAttribute("deals",deals);
        return "admin";
    }

    @GetMapping("/admin/sendmessage")
    public String getmessage(@RequestParam("dealid") Long id, Model model ) {
        Deal deal =  dealRepository.findById(id).get();
        model.addAttribute("deal",deal);
        return "sendmessage";
    }

    @PostMapping("/admin/sendmessage")
    public String sendmessage(@RequestParam("dealid") Long id,
                              @RequestParam("text") String text,
                              Model model) {
        Deal deal =  dealRepository.findById(id).get();
        Message message = new Message(deal.getUser(),"От администрации", text, new Date());
        messageRepository.saveAndFlush(message);
        return "redirect:/admin";
    }

    @GetMapping("/admin/product")
    public String get(Model model) {
        model.addAttribute("defaultProduct", new Product());
        model.addAttribute("categories", catRepository.findAll());
        return "addproduct";
    }

    @PostMapping("/admin/product")
    public String add(@RequestParam("id") Long id,
                      @RequestParam("category") Long catId,
                      @RequestParam("name") String name,
                      @RequestParam("cost") Double cost,
                      @RequestParam("description") String description,
                      @RequestParam("src") MultipartFile image) throws IOException {

        Long sid = null;
        Product product = null;

        if (image.getOriginalFilename().compareTo("") != 0)//Сохранить изображение есть пришло с формы
            sid = fileStorageService.storeFile(image).getId();

        if (id != 0) {//если редактирование
            product = productRepository.getOne(id);
            product.setCategory(catRepository.getOne(catId));
            product.setName(name);
            product.setCost(cost);
            product.setDescription(description);

            if (sid != null)
                product.setSrc(sid.toString());
        } else {//если добавление
            if (sid == null) {
                String noimage = loadImage("D:\\workspace\\src\\main\\resources\\static\\images\\noimage.jpg");
                product = new Product(name, cost, description, noimage, catRepository.getOne(catId));
            } else {
                product = new Product(name, cost, description, sid.toString(), catRepository.getOne(catId));
            }
            sendMessageToAll(product);
        }
        productRepository.saveAndFlush(product);
        return "redirect:/products?catId=" + product.getCategory().getId();
    }



    @RequestMapping(value = "/admin/deleteproduct", method = RequestMethod.POST)
    public String deleteProduct(@RequestParam("id") Long id, @RequestParam("category") Long catId) {
        Product product = productRepository.getOne(id);
        product.setCategory(catRepository.getOne(0L));
        productRepository.saveAndFlush(product);
        return "redirect:/products?catId=" + catId.toString();
    }

    @RequestMapping(value = "/admin/editproduct", method = RequestMethod.GET)
    public String editProduct(@RequestParam("id") Long id, Model model) {
        model.addAttribute("defaultProduct", productRepository.getOne(id));
        model.addAttribute("categories", catRepository.findAll());
        return "addproduct";
    }

    private void sendMessageToAll(Product product) {
        Message message = new Message();
        message.setDate(new Date());
        message.setHeader("Пополнение ассортимента");
        message.setText("В категорию " + product.getCategory().getName() + " поступили новые товары. ");
        for (User user : userService.findAll()) {
            message.setUser(user);
            messageRepository.saveAndFlush(message);
        }
    }

    public String loadImage(String pathName) {
        Path path = Paths.get(pathName);
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            System.out.println("\n_____cannot read bytes_____!");
        }

        MultipartFile result = new MockMultipartFile(path.getFileName().toString(),
                path.getFileName().toString(), "image/jpeg", content);
        try {
            return String.valueOf(imageService.storeFile(result).getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}