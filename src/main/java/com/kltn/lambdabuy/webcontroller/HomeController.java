/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kltn.lambdabuy.webcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Asus
 */
@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("/")
    public String test(Model theModel){
        return "views/home";
    }
    
    @GetMapping("/index")
    public String index(Model theModel){
        return "views/index";
    }
}
