package com.example.demo.web.controller;

import com.example.demo.persistence.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(@RequestParam("keyword")Optional<String> keyword, Model model) {
        List<Product> productList = productService.findByNameContaining(keyword.orElse(""));
        productList.forEach(System.out::println);
        model.addAttribute("productList", productList);
        return "index";
    }
}
