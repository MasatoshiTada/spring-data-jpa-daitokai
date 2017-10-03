package com.example.demo.persistence;

import com.example.demo.persistence.dto.ProductDto;
import com.example.demo.persistence.entity.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EntityManagerNativeTest {

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
    public void test_native_sql_entity() {
        Query query = em.createNativeQuery("select id, name, price, vendor_id, category_id, created_by, created_date, updated_by, updated_date from product where id = :id", Product.class);
        query.setParameter("id", 1);
        Product product = (Product) query.getSingleResult();
        assertThat(product.getId(), is(1));
        assertThat(product.getName(), is("パマ冷蔵庫"));
    }

    @Test
    public void test_native_sql_object_array() {
        Query query = em.createNativeQuery("select id, name from product where id = :id");
        query.setParameter("id", 1);
        Object[] objs = (Object[]) query.getSingleResult();
        Integer productId = (Integer) objs[0];
        String productName = (String) objs[1];
        assertThat(productId, is(1));
        assertThat(productName, is("パマ冷蔵庫"));
    }

    @Test
    public void test_sql_result_set_mapping() {
        Query query = em.createNativeQuery("select id, name from product where id = :id", "product_id_name");
        query.setParameter("id", 1);
        ProductDto productDto = (ProductDto) query.getSingleResult();
        assertThat(productDto.getId(), is(1));
        assertThat(productDto.getName(), is("パマ冷蔵庫"));
    }

    @Test
    public void test_jpql_native_function() {
        // H2 DatabaseのLEFT関数を利用
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE FUNCTION('LEFT', p.name, 2) = 'パマ'", Product.class);
        List<Product> productList = query.getResultList();
        assertThat(productList.size(), is(4));
    }

    @Test
    @Ignore // ストアドプロシージャ未作成のため実行できません
    public void test_stored_procedure() {
        StoredProcedureQuery query = em.createStoredProcedureQuery("プロシージャ名", Product.class);
        query.setParameter("パラメータ名", 1 /*値*/);
        List<Product> productList = (List<Product>) query.getResultList();
    }
}
