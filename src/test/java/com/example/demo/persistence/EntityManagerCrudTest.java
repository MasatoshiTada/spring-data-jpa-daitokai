package com.example.demo.persistence;

import com.example.demo.persistence.entity.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class EntityManagerCrudTest {

    /**
     * LocalContainerEntityManagerFactoryBeanによってBean定義されている
     */
    @Autowired
    EntityManagerFactory emf;

    EntityManager em;

    @Before
    public void setUp() {
        em = emf.createEntityManager();
    }

    @After
    public void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @BeforeTransaction
    public void setUpBeforeTransaction() {
        // @Beforeより前の、トランザクション開始前に実行される
    }

    @AfterTransaction
    public void tearDownAfterTransaction() {
        // @Afterより後の、トランザクション終了後に実行される
    }

    @Test
    public void test_find() {
        Product product = em.find(Product.class, 1);
        assertThat(product.getName(), is("パマ冷蔵庫"));
        assertTrue(em.contains(product)); // MANAGED状態ならtrue
    }

    @Test
    public void test_persist() {
        em.getTransaction().begin();
        Product newProduct = new Product("れいぞうこ", 100000L);
        em.persist(newProduct);
        em.flush();
        assertTrue(em.contains(newProduct)); // MANAGED状態ならtrue
        assertThat(newProduct.getId(), is(100));

        Product productFromDb = em.find(Product.class, 100);
        assertThat(productFromDb.getName(), is("れいぞうこ"));
        assertThat(productFromDb.getPrice(), is(100000L));
    }

    @Test
    public void test_update() {
        em.getTransaction().begin();
        Product product = em.find(Product.class, 1);
        product.setName("更新後の名前");
        em.flush();
        String productName = (String) em.createNativeQuery("SELECT name FROM product WHERE id = :id")
                .setParameter("id", 1)
                .getSingleResult();
        assertThat(productName, is("更新後の名前"));
        assertThat(product.getUpdatedBy(), is("user01"));
        assertNotNull(product.getUpdatedDate());
        em.getTransaction().rollback();
    }

    @Test
    public void test_update_by_merge() {
        em.getTransaction().begin();
        Product product = new Product();
        product.setId(1);
        product.setName("更新後の名前");
        Product mergedProduct = em.merge(product);
        em.flush();
        String productName = (String) em.createNativeQuery("SELECT name FROM product WHERE id = :id")
                .setParameter("id", 1)
                .getSingleResult();
        assertThat(productName, is("更新後の名前"));
        assertTrue(em.contains(mergedProduct));
        assertFalse(em.contains(product));
        em.getTransaction().rollback();
    }

    @Test
    public void test_insert_by_merge() {
        em.getTransaction().begin();
        Product product = new Product();
        product.setId(999);
        product.setName("更新後の名前");
        Product mergedProduct = em.merge(product);
        em.flush();
        String productName = (String) em.createNativeQuery("SELECT name FROM product WHERE id = :id")
                .setParameter("id", 101) // 自動採番されたIDになる
                .getSingleResult();
        assertThat(productName, is("更新後の名前"));
        assertTrue(em.contains(mergedProduct));
        assertFalse(em.contains(product));
        em.getTransaction().rollback();
    }

    @Test
    public void test_remove() {
        em.getTransaction().begin();
        Product product = em.find(Product.class, 30);
        em.remove(product);
        em.flush();
        BigInteger count = (BigInteger) em.createNativeQuery("SELECT count(*) FROM product WHERE id = :id")
                .setParameter("id", 30)
                .getSingleResult();
        assertThat(count, is(BigInteger.ZERO));
        em.getTransaction().rollback();
    }

    @Test
    public void test_remove_cancel_by_rollback() {
        em.getTransaction().begin();
        Product product = em.find(Product.class, 30);
        em.remove(product);
        em.flush();
        em.getTransaction().rollback();
        BigInteger count = (BigInteger) em.createNativeQuery("SELECT count(*) FROM product WHERE id = :id")
                .setParameter("id", 30)
                .getSingleResult();
        assertThat(count, is(BigInteger.ONE));
    }

    @Test
    public void test_remove_cancel_by_detach() {
        em.getTransaction().begin();
        Product product = em.find(Product.class, 30);
        em.remove(product);
        em.detach(product);
        em.flush();
        BigInteger count = (BigInteger) em.createNativeQuery("SELECT count(*) FROM product WHERE id = :id")
                .setParameter("id", 30)
                .getSingleResult();
        assertThat(count, is(BigInteger.ONE));
        em.getTransaction().rollback();
    }

    @Test
    public void test_remove_cancel_by_clear() {
        em.getTransaction().begin();
        Product product = em.find(Product.class, 30);
        em.remove(product);
        em.clear();
        em.flush();
        BigInteger count = (BigInteger) em.createNativeQuery("SELECT count(*) FROM product WHERE id = :id")
                .setParameter("id", 30)
                .getSingleResult();
        assertThat(count, is(BigInteger.ONE));
        em.getTransaction().rollback();
    }
}
