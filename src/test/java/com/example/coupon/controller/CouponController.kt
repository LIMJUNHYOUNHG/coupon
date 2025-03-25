class CouponControllerTest : StringSpec({

    val couponService = mockk<com.example.coupon.service.CouponService>()
    val couponController = com.example.coupon.controller.CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: A couponId and the couponService returning true for successful redemption.
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon is called with the couponId.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK and the body should be "쿠폰 사용 성공".
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest with error message when no coupons are available" {
        // Given: A couponId and the couponService returning false for failed redemption due to no available coupons.
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon is called with the couponId.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BadRequest and the body should be "잔여 쿠폰 없음".
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon creation is successful" {
        // Given: A CouponCreate object.
        val couponCreate = com.example.coupon.controller.CouponCreate("Test Coupon", 1000, 10L)
        every { couponService.create(couponCreate) } returns Unit // Void method, so return Unit

        // When: createCoupon is called with the couponCreate object.
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK and the body should be "쿠폰 생성 성공".
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})