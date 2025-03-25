class CouponControllerTest : StringSpec({

    val couponService = mockk<com.example.coupon.service.CouponService>()
    val couponController = com.example.coupon.controller.CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: A coupon ID and the coupon service returns true for successful redemption
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK and the body should contain the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BAD_REQUEST with failure message when coupon redemption fails" {
        // Given: A coupon ID and the coupon service returns false for unsuccessful redemption
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BAD_REQUEST and the body should contain the failure message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon is created successfully" {
        // Given: A coupon create request and the coupon service create function is mocked
        val couponCreate = com.example.coupon.controller.CouponCreate("Test Coupon", 1000, 10)
        every { couponService.create(couponCreate) } returns Unit

        // When: createCoupon is called with the coupon create request
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK and the body should contain the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})