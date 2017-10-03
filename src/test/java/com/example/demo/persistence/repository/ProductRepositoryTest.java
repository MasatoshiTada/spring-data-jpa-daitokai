package com.example.demo.persistence.repository;

import com.example.demo.persistence.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void test_findByIdJoinFetch() {
        Product product = productRepository.findByIdJoinFetch(1);
        assertThat(product.getName(), is("パマ冷蔵庫"));
        assertThat(product.getVendor().getName(), is("パマソニック"));
        assertThat(product.getCategory().getName(), is("冷蔵庫"));
    }

    @Test
    @Transactional
    public void test_updateName() {
        productRepository.updateName("パママ冷蔵庫", 1);
        Product product = productRepository.getOne(1);
        assertThat(product.getName(), is("パママ冷蔵庫"));
        assertThat(product.getCreatedBy(), is("user00"));
        assertNotNull(product.getCreatedDate());
        // JPQLでUPDATEした場合はEntityListenerが動かないため更新者・更新日時は保存されない
        assertNull(product.getUpdatedBy());
        assertNull(product.getUpdatedDate());
    }

    @Test
    public void test_findByNameContains() {
        List<Product> productList = productRepository.findByNameContaining("パマ");
        assertThat(productList.size(), is(4));
    }

    /**
     * Spring Data JPAではDataAccessExceptionのサブクラスに変換される
     */
    @Test(expected = InvalidDataAccessResourceUsageException.class)
    public void test_findByIdWithBadGrammar() {
        Product product = productRepository.findByIdWithBadGrammar(1);
    }

    /**
     * Spring Data JPAを使っていないので例外変換されない
     */
    @Test(expected = PersistenceException.class)
    public void test_bad_sql_by_EntityManager() {
        // わざと間違えたSQL
        Object obj = em.createNativeQuery("SELECTxxxxx * FROM product p JOIN vendor v ON p.vendor_id = v.id WHERE p.id = :id")
                .setParameter("id", 1)
                .getSingleResult();
    }

    /**
     * Spring Data JPAではDataAccessExceptionのサブクラスに変換される
     */
    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void test_find_too_many_result() {
        Product product = productRepository.find();
    }

    /**
     * Spring Data JPAを使っていないので例外変換されない
     */
    @Test(expected = PersistenceException.class)
    public void test_find_too_many_result_by_EntityManager() {
        Product product = em.createQuery("SELECT p FROM Product p", Product.class)
                .getSingleResult();
    }

    @Test
    @Transactional
    public void test_audit_insert() {
        Product product = new Product("れいぞうこ", 80000L);
        productRepository.save(product);
        Object[] objs = (Object[]) em.createNativeQuery("SELECT name, created_by, created_date FROM product p WHERE p.name = :name")
                .setParameter("name", "れいぞうこ")
                .getSingleResult();
        assertThat(objs[0], is("れいぞうこ"));
        assertThat(objs[1], is("user01"));
        assertNotNull(objs[2]);
    }

    @Test
    @Transactional
    public void test_audit_update() {
        Product product = productRepository.getOne(1);
        product.setName("れいぞうこ");
        productRepository.save(product);
        Object[] objs = (Object[]) em.createNativeQuery("SELECT name, created_by, created_date, updated_by, updated_date FROM product p WHERE p.id = :id")
                .setParameter("id", 1)
                .getSingleResult();
        assertThat(objs[0], is("れいぞうこ"));
        assertThat(objs[1], is("user00"));
        assertNotNull(objs[2]);
        assertThat(objs[3], is("user01"));
        assertNotNull(objs[4]);
    }
}
