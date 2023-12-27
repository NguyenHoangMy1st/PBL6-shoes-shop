package com.dnanh01.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dnanh01.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
        @Query("SELECT p FROM Product p " +
                        "WHERE (p.brand.name = :brand OR :brand = '') " +
                        "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) "
                        +
                        "AND (:minDiscount IS NULL OR p.discountPersent >= :minDiscount) " +
                        "ORDER BY " +
                        "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
                        "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC")
        public List<Product> filterProducts(
                        @Param("brand") String brand,
                        @Param("minPrice") Integer minPrice,
                        @Param("maxPrice") Integer maxPrice,
                        @Param("minDiscount") Integer minDiscount,
                        @Param("sort") String sort);

        // --------------------DASHBOARD ADMIN--------------------

        // New Product
        @Query("SELECT p FROM Product p " +
                        "WHERE p.createAt = (SELECT MAX(p2.createAt) FROM Product p2 WHERE p2.quantity > 0) " +
                        "AND p.quantity > 0")
        Product findLatestProduct();
        // Top Rate

}
