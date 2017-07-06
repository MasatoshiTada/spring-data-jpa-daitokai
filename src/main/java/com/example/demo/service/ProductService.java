package com.example.demo.service;


import com.example.demo.persistence.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> findByNameContaining(String nameKeyword);
}
