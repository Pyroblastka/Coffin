package com.pyro.api;

import com.pyro.entities.Product;
import com.pyro.entities.User;
import com.pyro.repositories.CatRepository;
import com.pyro.repositories.ProductRepository;
import com.pyro.repositories.RoleRepository;
import com.pyro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DataController {
    @Autowired
    UserService userService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CatRepository catRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/adduser")
    Object met(@RequestParam(value = "name") String name,
               @RequestParam(value = "password") String password) {
        User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        userService.save(user);

        Map map = new HashMap();
        map.put("Users", userService.findAll());
        map.put("Roles", roleRepository.findAll());
        //user.setRoles(Arrays.asList(roleRepository.getOne(0L)));

        return map;
    }

    @RequestMapping("/searchq")
    public List<Product>  search(@RequestParam(value = "value") String value) throws IOException {
        try{

            List<Product> products = productRepository.findByNameContainsIgnoreCase(value);
            products.removeIf(product -> product.getCategory().getName().compareTo("deleted")==0);
            return products;
        }
        catch (Exception e){
            return null;
        }
    }
}
