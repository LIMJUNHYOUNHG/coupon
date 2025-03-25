class CouponControllerTest : StringSpec({

    val couponService = mockk<com.example.coupon.service.CouponService>()
    val couponController = com.example.coupon.controller.CouponController(couponService)

    "redeemCoupon - Successful redemption" {
        // Given: A coupon ID and the coupon service returning true for successful redemption
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: Calling the redeemCoupon method
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon - Unsuccessful redemption (no coupons left)" {
        // Given: A coupon ID and the coupon service returning false for unsuccessful redemption
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: Calling the redeemCoupon method
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be a Bad Request with the 'no coupons left' message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon - Successful creation" {
        // Given: A CouponCreate object
        val couponCreate = com.example.coupon.controller.CouponCreate("Test Coupon", 100, 10L)
        every { couponService.create(couponCreate) } returns Unit

        // When: Calling the createCoupon method
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK with the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})