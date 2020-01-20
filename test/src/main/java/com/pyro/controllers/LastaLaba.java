package com.pyro.controllers;


import com.pyro.entities.PostgreSqlExample;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
public class LastaLaba {


    PostgreSqlExample tableRepository = PostgreSqlExample.getInstance();


/*
    @PostMapping("/laba")
    public String get(@RequestParam("table") String tablename,
                      Model model) throws SQLException {
        List<Col>  list = tableRepository.describeTabe(tablename);
        model.addAttribute("cols",list);
        model.addAttribute("tbls",tableRepository.getTables());

        return "laba";
    }
*/
    @GetMapping("/laba")
    public String add(Model model) throws SQLException {
        model.addAttribute("state",1);
        model.addAttribute("datnames",tableRepository.getDB());
        //model.addAttribute("tbls",tableRepository.getTables());
        return "laba";
    }

    @PostMapping("/laba")
    public String qweee(@RequestParam( value = "datname" , required = false) String datname,
                      @RequestParam( value = "table", required= false) String tablename,
                      Model model) throws SQLException {
        if(datname != null)
        {

            tableRepository.reconnect(datname);
        }
        model.addAttribute("state",2);
        model.addAttribute("cols",tableRepository.describeTabe(tablename));
        //model.addAttribute("datnames",tableRepository.getDB());
        model.addAttribute("tbls",tableRepository.getTables());
        return "laba";
    }

    @GetMapping("/laba2")
    public String qweqwa(Model model) throws SQLException {
        model.addAttribute("users", tableRepository.getUsers());
        return "laba2";
    }

    @PostMapping("/laba2")
    public String qqwe(@RequestParam( value = "username" , required = false) String username,
                        @RequestParam( value = "password", required= false) String password,
                       @RequestParam( value = "insert", required= false) boolean insert,
                       @RequestParam( value = "update", required= false) boolean update,
                       @RequestParam( value = "delete", required= false) boolean delete,
                        Model model) throws SQLException {

            tableRepository.createUser(username,password,insert,update,delete);

        return "redirect: /laba2";
    }

    @PostMapping("/laba3")
    public String qqqweqwa(@RequestParam( value = "user", required= false) String user, Model model) throws SQLException {
        tableRepository.deleteUser(user);
        return "redirect: /laba2";
    }
}
