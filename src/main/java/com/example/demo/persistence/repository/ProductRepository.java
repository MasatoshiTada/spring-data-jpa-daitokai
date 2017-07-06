package com.example.demo.persistence.repository;

import com.example.demo.persistence.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @EntityGraph()
    @Query("SELECT p FROM Product p JOIN FETCH p.vendor JOIN FETCH p.category WHERE p.id = :id")
    Product findByIdJoinFetch(@Param("id") Integer id);

    @Query("SELECT p FROM Product p JOIN p.vendor JOIN p.category WHERE p.id = :id")
    Product findByIdJoin(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Product p SET p.name = :name WHERE p.id = :id")
    void updateName(@Param("name") String name, @Param("id") Integer id);
}
