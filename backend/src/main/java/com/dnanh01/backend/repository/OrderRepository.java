package com.dnanh01.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dnanh01.backend.model.Order;
import com.dnanh01.backend.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

        @Query("SELECT o FROM Order o " +
                        "WHERE o.user.id = :userId " +
                        "AND (o.orderStatus = 'PLACED' OR o.orderStatus = 'CONFIRMED' OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'DELIVERED')")
        public List<Order> getUsersOrders(@Param("userId") Long userId);

        public List<Order> findByUser(User user);

        @Query("SELECT o FROM Order o " +
                        "WHERE o.user.id = :userId " +
                        "AND (o.orderStatus = 'CONFIRMED')")
        public List<Order> getConfirmedOrdersForUser(@Param("userId") Long userId);

        // --------------------dashboard admin--------------------

        @Query("SELECT DAYOFWEEK(o.createAt) as `day`, SUM(o.totalDiscountedPrice) as revenue " +
                        "FROM Order o " +
                        "WHERE o.orderStatus = 'CONFIRMED' " +
                        "GROUP BY day")
        public List<Object[]> getWeeklyRevenue();

        @Query("SELECT MONTH(o.createAt) as month, SUM(o.totalDiscountedPrice) as revenue " +
                        "FROM Order o " +
                        "WHERE o.orderStatus = 'CONFIRMED' " +
                        "GROUP BY month")
        public List<Object[]> getMonthlyRevenue();

        @Query("SELECT COALESCE(SUM(o.totalPrice), 0) " +
                        "FROM Order o " +
                        "WHERE o.orderStatus = 'CONFIRMED' " +
                        "AND MONTH(o.createAt) = MONTH(CURRENT_DATE) " +
                        "AND YEAR(o.createAt) = YEAR(CURRENT_DATE)")
        double getTotalRevenueForCurrentMonth();

        @Query("SELECT COUNT(DISTINCT o.user.id) " +
                        "FROM Order o " +
                        "WHERE o.orderStatus = 'CONFIRMED' " +
                        "AND MONTH(o.createAt) = MONTH(CURRENT_DATE) " +
                        "AND YEAR(o.createAt) = YEAR(CURRENT_DATE)")
        public int getTotalUsersForCurrentMonth();

        @Query("SELECT COUNT(o) FROM Order o " +
                        "WHERE o.orderStatus = 'CONFIRMED' " +
                        "AND MONTH(o.createAt) = MONTH(CURRENT_DATE) " +
                        "AND YEAR(o.createAt) = YEAR(CURRENT_DATE)")
        public int getTotalConfirmedOrdersForCurrentMonth();

        @Query("SELECT COALESCE(SUM(oi.discountedPrice - p.warehousePrice), 0) " +
                        "FROM OrderItem oi " +
                        "JOIN oi.product p " +
                        "JOIN oi.order o " +
                        "WHERE o.orderStatus = 'CONFIRMED' " +
                        "AND MONTH(o.createAt) = MONTH(CURRENT_DATE) " +
                        "AND YEAR(o.createAt) = YEAR(CURRENT_DATE)")
        public double getTotalProfitForCurrentMonth();
}
