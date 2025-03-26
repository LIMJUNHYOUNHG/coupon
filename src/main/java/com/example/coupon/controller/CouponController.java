package com.example.coupon.controller;

import com.example.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/{couponId}/redeem")
    public ResponseEntity<String> redeemCoupon(@PathVariable Long couponId) {
        boolean success = couponService.redeemCoupon(couponId);
        if (success) {
            String responseMessage = "쿠폰 사용 성공";
            return ResponseEntity.ok(responseMessage);
        } else {
            String responseMessage = "잔여 쿠폰 없음";
            return ResponseEntity.badRequest().body(responseMessage);
        }
    }

    @PostMapping
    public ResponseEntity<String> createCoupon(@RequestBody CouponCreate couponCreate) {
        couponService.create(couponCreate);
        String responseMessage = "쿠폰 생성 성공";
        return ResponseEntity.ok().body(responseMessage);
    }
}
