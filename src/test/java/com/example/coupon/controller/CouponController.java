"redeemCoupon should return OK when coupon is successfully redeemed" {
        // Given:
        val couponId: Long = 123
        val couponService = mockk<CouponService>()
        val couponController = CouponController(couponService)

        every { couponService.redeemCoupon(couponId) } returns true

        // When:
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then:
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest when no coupons are left" {
        // Given:
        val couponId: Long = 456
        val couponService = mockk<CouponService>()
        val couponController = CouponController(couponService)

        every { couponService.redeemCoupon(couponId) } returns false

        // When:
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then:
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK when coupon is successfully created" {
        // Given:
        val couponCreate = CouponCreate("SummerSale", 10, 100L)
        val couponService = mockk<CouponService>(relaxed = true) // relaxed = true for void methods
        val couponController = CouponController(couponService)

        every { couponService.create(couponCreate) } returns Unit

        // When:
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then:
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})