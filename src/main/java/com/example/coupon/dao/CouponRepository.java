package com.example.coupon.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    @Modifying
    @Query("UPDATE Coupon c SET c.count = c.count - 1 WHERE c.id = :couponId AND c.count > 0")
    int decrementCouponCount(@Param("couponId") Long couponId);
}
