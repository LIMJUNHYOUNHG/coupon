"redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given:
        val couponId: Long = 1L
        val couponService: CouponService = mockk()
        val couponController = CouponController(couponService)

        every { couponService.redeemCoupon(couponId) } returns true

        // When:
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then:
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"

        verify(exactly = 1) { couponService.redeemCoupon(couponId) }
    }

    "redeemCoupon should return BadRequest with error message when coupon redemption fails" {
        // Given:
        val couponId: Long = 1L
        val couponService: CouponService = mockk()
        val couponController = CouponController(couponService)

        every { couponService.redeemCoupon(couponId) } returns false

        // When:
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then:
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"

        verify(exactly = 1) { couponService.redeemCoupon(couponId) }
    }

    "createCoupon should return OK with success message when coupon is created successfully" {
        // Given:
        val couponCreate = CouponCreate("Test Coupon", 100, 5L)
        val couponService: CouponService = mockk()
        val couponController = CouponController(couponService)

        every { couponService.create(couponCreate) } returns Unit // void method라서 Unit

        // When:
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then:
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"

        verify(exactly = 1) { couponService.create(couponCreate) }
    }
})