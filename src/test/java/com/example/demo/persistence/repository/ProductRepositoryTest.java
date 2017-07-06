package com.example.demo.persistence.repository;

import com.example.demo.persistence.entity.Product;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@DataJpaTest
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test(expected = LazyInitializationException.class)
    public void findOneで商品が1件LAZYで取得できる() {
        Product product = productRepository.findOne(1);
        assertThat(product.getName(), is("パマ冷蔵庫"));
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        assertThat(persistenceUtil.isLoaded(product, "vendor"), is(false));
        assertThat(persistenceUtil.isLoaded(product, "category"), is(false));
        // トランザクション外なので遅延読み込みできない
        assertThat(product.getVendor().getName(), is("パマソニック"));
        assertThat(product.getCategory().getName(), is("冷蔵庫"));
    }

    @Test
    @Transactional
    public void findOneで商品が1件LAZYで取得できる_Transactional() {
        Product product = productRepository.findOne(1);
        assertThat(product.getName(), is("パマ冷蔵庫"));
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        assertThat(persistenceUtil.isLoaded(product, "vendor"), is(false));
        assertThat(persistenceUtil.isLoaded(product, "category"), is(false));
        // トランザクション外なので遅延読み込み可能
        assertThat(product.getVendor().getName(), is("パマソニック"));
        assertThat(product.getCategory().getName(), is("冷蔵庫"));
    }

    @Test
    public void findOneで商品が1件JOIJNで取得できる() {
        Product product = productRepository.findByIdJoin(1);
        assertThat(product.getName(), is("パマ冷蔵庫"));
        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        assertThat(persistenceUtil.isLoaded(product, "vendor"), is(false));
        assertThat(persistenceUtil.isLoaded(product, "category"), is(false));
    }

    @Test
    @Transactional
    public void findOneで商品が1件EAGERで取得できる() {
        Product product = productRepository.findByIdJoinFetch(1);
        assertThat(product.getName(), is("パマ冷蔵庫"));
        assertThat(product.getVendor().getName(), is("パマソニック"));
        assertThat(product.getCategory().getName(), is("冷蔵庫"));
    }

    @Test
    @Transactional
    public void test_update_name() {
        productRepository.updateName("パママ冷蔵庫", 1);

        Product product = productRepository.findOne(1);
        assertThat(product.getName(), is("パママ冷蔵庫"));
    }
}
