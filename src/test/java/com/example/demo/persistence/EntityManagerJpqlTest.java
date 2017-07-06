package com.example.demo.persistence;

import com.example.demo.dto.CountDto;
import com.example.demo.persistence.entity.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@RunWith(SpringRunner.class)
public class EntityManagerJpqlTest {

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
    public void test_select_one_jpql() {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.id = :id", Product.class);
        query.setParameter("id", 1);
        Product product = query.getSingleResult();
        assertThat(product.getName(), is("パマ冷蔵庫"));
    }

    @Test
    public void test_select_list_jpql() {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.id <= :maxId ORDER BY p.id", Product.class);
        query.setParameter("maxId", 5);
        List<Product> productList = query.getResultList();
        assertThat(productList.size(), is(5));
    }

    @Test
    public void test_jpq_constructor_expression () {
        TypedQuery<CountDto> query = em.createQuery("SELECT new com.example.demo.dto.CountDto(od.product.id, COUNT(od)) FROM OrderDetail od GROUP BY od.product.id", CountDto.class);
        List<CountDto> countDtoList = query.getResultList();
        assertThat(countDtoList.size(), is(3));
        for (CountDto countDto : countDtoList) {
            System.out.println(countDto);
        }
    }
}
