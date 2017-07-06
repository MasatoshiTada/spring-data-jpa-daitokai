package com.example.demo.service.impl;

import com.example.demo.persistence.entity.Product;
import com.example.demo.persistence.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findByNameContaining(String nameKeyword) {
        return productRepository.findByNameContaining(nameKeyword);
    }
}
