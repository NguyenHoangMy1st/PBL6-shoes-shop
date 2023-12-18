package com.dnanh01.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.dnanh01.backend.dto.RevenueOrProfitStatsDto;
import com.dnanh01.backend.model.Order;
import com.dnanh01.backend.model.User;

import jakarta.persistence.QueryHint;

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

        // line chart
        @Query(value = "WITH HourReference AS ( " +
                        "     SELECT 0 AS hour_of_day " +
                        "     UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 " +
                        "     UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 " +
                        "     UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 " +
                        "     UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 " +
                        "     UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 " +
                        "     UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 " +
                        "     UNION SELECT 19 UNION SELECT 20 UNION SELECT 21 " +
                        "     UNION SELECT 22 UNION SELECT 23 " +
                        ") " +
                        "SELECT " +
                        "    hr.hour_of_day AS " + RevenueOrProfitStatsDto.TIME_POINT + ", " +
                        "    COALESCE(SUM(revenue_subquery.total_discounted_price), 0) AS "
                        + RevenueOrProfitStatsDto.REVENUE + ", " +
                        "    COALESCE(SUM(profit_subquery.total_profit), 0) AS " + RevenueOrProfitStatsDto.PROFIT + " "
                        +
                        "FROM " +
                        "    HourReference hr " +
                        "LEFT JOIN " +
                        "    ( " +
                        "        SELECT " +
                        "            DATE_FORMAT(o1.order_date, '%H') AS hour_of_day, " +
                        "            o1.total_discounted_price " +
                        "        FROM " +
                        "            orders o1 " +
                        "        WHERE " +
                        "            DATE_FORMAT(o1.order_date, '%d/%m/%Y') = :selectedDay " +
                        "            AND o1.order_status = 'DELIVERED' " +
                        "    ) revenue_subquery ON hr.hour_of_day = revenue_subquery.hour_of_day " +
                        "LEFT JOIN " +
                        "    ( " +
                        "        SELECT " +
                        "            DATE_FORMAT(o2.order_date, '%H') AS hour_of_day, " +
                        "            SUM((p.discounted_price - p.warehouse_price) * oi.quantity) AS total_profit " +
                        "        FROM " +
                        "            orders o2 " +
                        "            JOIN order_item oi ON o2.id = oi.order_id " +
                        "            JOIN product p ON oi.product_id = p.id " +
                        "        WHERE " +
                        "            DATE_FORMAT(o2.order_date, '%d/%m/%Y') = :selectedDay " +
                        "            AND o2.order_status = 'DELIVERED' " +
                        "        GROUP BY " +
                        "            hour_of_day " +
                        "    ) profit_subquery ON hr.hour_of_day = profit_subquery.hour_of_day " +
                        "GROUP BY " +
                        "    hr.hour_of_day " +
                        "ORDER BY " +
                        "    hr.hour_of_day;", nativeQuery = true)
        @QueryHints(value = { @QueryHint(name = "org.hibernate.readOnly", value = "true") })
        public List<Object[]> getStatsForSelectedDayRevenueAndProfit(@Param("selectedDay") String selectedDay);
        // stats
}
