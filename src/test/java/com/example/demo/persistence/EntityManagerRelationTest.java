package com.example.demo.persistence;

import com.example.demo.persistence.entity.*;
import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

import javax.persistence.*;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EntityManagerRelationTest {

    /**
     * LocalContainerEntityManagerFactoryBeanによってBean定義されている
     */
    @Autowired
    EntityManagerFactory emf;

    EntityManager em;

    PersistenceUnitUtil util;

    @Before
    public void setUp() {
        em = emf.createEntityManager();
        util = emf.getPersistenceUnitUtil();
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
    public void test_lazy() {
        Product product = em.find(Product.class, 1);
        assertFalse(util.isLoaded(product, "vendor")); // まだvendorは読み込まれていない
        assertFalse(util.isLoaded(product, "category")); // まだcategoryは読み込まれていない
        System.out.println("getter呼ぶ前");
        Vendor vendor = product.getVendor();
        Category category = product.getCategory();
        System.out.println("assertする前");
        assertThat(vendor.getName(), is("パマソニック")); // ここでvendorへのSELECT文が実行される
        assertThat(category.getName(), is("冷蔵庫")); // ここでcategoryへのSELECT文が実行される
        assertTrue(util.isLoaded(product, "vendor")); // vendorが読み込まれている
        assertTrue(util.isLoaded(product, "category")); // categoryが読み込まれている
    }

    @Test(expected = LazyInitializationException.class)
    public void test_lazy_out_of_context() {
        em.getTransaction().begin(); // トランザクション開始

        Product product = em.find(Product.class, 1);
        assertFalse(util.isLoaded(product, "vendor")); // まだvendorは読み込まれていない
        assertFalse(util.isLoaded(product, "category")); // まだcategoryは読み込まれていない
        em.getTransaction().rollback(); // トランザクションが終了すると永続化コンテキストが破棄される

        Vendor vendor = product.getVendor(); // ここではまだ例外は発生しない
        String vendorName = vendor.getName(); // ここで例外発生
    }

    @Test
    public void test_eager() {
        ProductEager product = em.find(ProductEager.class, 1); // product,vendor,categoryへのSELECT文が実行される
        assertTrue(util.isLoaded(product, "vendor")); // 既にvendorは読み込まれている
        assertTrue(util.isLoaded(product, "category")); // 既にcategoryは読み込まれている
        Vendor vendor = product.getVendor();
        Category category = product.getCategory();
        assertThat(category.getName(), is("冷蔵庫"));
        assertThat(vendor.getName(), is("パマソニック"));
    }

    @Test
    public void test_n_plus_1_problem() {
        // ここでorder_summaryへのSELECTが発行される（1回）
        OrderSummary orderSummary = em.find(OrderSummary.class, 1);
        assertFalse(util.isLoaded(orderSummary, "orderDetailList")); // まだorderDetailListは読み込まれていない
        System.out.println(orderSummary);
        List<OrderDetail> orderDetailList = orderSummary.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            // ここでorder_detailへのSELECTが発行される（1回）
            System.out.println(orderDetail);
        }
        assertTrue(util.isLoaded(orderSummary, "orderDetailList")); // まだorderDetailListは読み込まれていない
    }

    @Test
    public void test_join() {
        // ここでorder_summaryへのSELECTが発行される（1回）
        // order_summaryとorder_detailがJOINされてはいるが、order_detailの列は一切読み込まれない
        List<OrderSummary> orderSummaryList = em.createQuery("SELECT os FROM OrderSummary os LEFT JOIN os.orderDetailList", OrderSummary.class)
                .getResultList();
        assertThat(orderSummaryList.size(), is(14));
        for (OrderSummary orderSummary : orderSummaryList) {
//            assertTrue(util.isLoaded(orderSummary, "orderDetailList")); // まだorderDetailListは読み込まれていない = JOINではN+1問題は解決しない
            System.out.println(orderSummary);
            List<OrderDetail> orderDetailList = orderSummary.getOrderDetailList();
            for (OrderDetail orderDetail : orderDetailList) {
                // ここでorder_detailへのSELECTが発行される（N回）
                System.out.println(orderDetail);
            }
            assertTrue(util.isLoaded(orderSummary, "orderDetailList")); // まだorderDetailListは読み込まれていない
        }
    }

    /**
     * order_summaryのIDが1,2のものはorder_detailが3件ずつある（＝計6件）。
     * order_detailを持たないorder_summaryが残り8件あるので、
     * 検索結果のリストのサイズは合計で6 + 8 = 14件になる。
     * (コンソール出力参照)
     */
    @Test
    public void test_join_fetch() {
        // ここでorder_summaryへのSELECTが発行される（1回）
        List<OrderSummary> orderSummaryList = em.createQuery("SELECT os FROM OrderSummary os LEFT JOIN FETCH os.orderDetailList", OrderSummary.class)
                .getResultList();
        assertThat(orderSummaryList.size(), is(14));
        for (OrderSummary orderSummary : orderSummaryList) {
            assertTrue(util.isLoaded(orderSummary, "orderDetailList")); // orderDetailListは読み込み済み = N+1問題が解決した！
            System.out.println(orderSummary);
            List<OrderDetail> orderDetailList = orderSummary.getOrderDetailList();
            for (OrderDetail orderDetail : orderDetailList) {
                // ここではSELECTは発行されない
                System.out.println(orderDetail);
                assertFalse(util.isLoaded(orderDetail, "product")); // productは読み込まれていない
            }
        }
    }

    /**
     * DISTINCTキーワードを付けると、OrderSummaryに対する重複が無くなる。
     */
    @Test
    public void test_join_fetch_distinct() {
        // ここでorder_summaryへのSELECTが発行される（1回）
        List<OrderSummary> orderSummaryList = em.createQuery("SELECT DISTINCT os FROM OrderSummary os LEFT JOIN FETCH os.orderDetailList", OrderSummary.class)
                .getResultList();
        assertThat(orderSummaryList.size(), is(10));
        for (OrderSummary orderSummary : orderSummaryList) {
            assertTrue(util.isLoaded(orderSummary, "orderDetailList")); // orderDetailListは読み込み済み = N+1問題が解決した！
            System.out.println(orderSummary);
            List<OrderDetail> orderDetailList = orderSummary.getOrderDetailList();
            for (OrderDetail orderDetail : orderDetailList) {
                // ここではSELECTは発行されない
                System.out.println(orderDetail);
                assertFalse(util.isLoaded(orderDetail, "product")); // productは読み込まれていない
            }
        }
    }

    @Test
    public void test_join_fetch_nested() {
        // ここでorder_summaryへのSELECTが発行される（1回、order_detailやproductもJOINされている）
        // os.orderDetailListに対してエイリアスodを指定できる（Hibernateのみ）
        OrderSummary orderSummary = em.createQuery("SELECT os FROM OrderSummary os JOIN FETCH os.orderDetailList od JOIN FETCH od.product p WHERE os.id = :id", OrderSummary.class)
                .setParameter("id", 1)
                .getSingleResult();
        assertTrue(util.isLoaded(orderSummary, "orderDetailList"));
        System.out.println(orderSummary);
        List<OrderDetail> orderDetailList = orderSummary.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            // ここではSELECTは発行されない
            System.out.print(orderDetail);
            assertTrue(util.isLoaded(orderDetail, "product")); // productは読み込み済み
            System.out.print(" " + orderDetail.getProduct());
            System.out.println();
        }
    }

}
