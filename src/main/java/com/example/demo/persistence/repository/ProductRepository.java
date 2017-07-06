package com.example.demo.persistence.repository;

import com.example.demo.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p JOIN FETCH p.vendor JOIN FETCH p.category WHERE p.id = :id")
    Product findByIdJoinFetch(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Product p SET p.name = :name WHERE p.id = :id")
    void updateName(@Param("name") String name, @Param("id") Integer id);

    List<Product> findByNameContaining(String nameKeyword);

    /**
     * 例外発生のサンプルのため、わざと間違えたネイティブSQLを書いている
     */
    @Query(value = "SELECTxxxxx * FROM product p JOIN vendor v ON p.vendor_id = v.id WHERE p.id = :id", nativeQuery = true)
    Product findByIdWithBadGrammar(@Param("id") Integer id);

    /**
     * 例外発生のサンプルのため、単一検索と複数検索をわざと間違えている
     */
    @Query("SELECT p FROM Product p")
    Product find();
}
