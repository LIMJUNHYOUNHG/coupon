package com.example.coupon.service;

import com.example.coupon.controller.CouponCreate;
import com.example.coupon.dao.Coupon;
import com.example.coupon.dao.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public boolean redeemCoupon(final Long couponId) {
        int updatedCount = couponRepository.decrementCouponCount(couponId);
        return updatedCount > 0;
    }

    @Transactional
    public void create(final CouponCreate couponCreate) {
        Coupon coupon = Coupon.builder()
                .count(couponCreate.count())
                .name(couponCreate.name())
                .build();

        couponRepository.save(coupon);
    }

}
