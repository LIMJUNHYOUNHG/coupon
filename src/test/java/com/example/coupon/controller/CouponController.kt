class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: A coupon ID and the coupon service returns true for successful redemption.
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: Calling the redeemCoupon endpoint.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with the correct success message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest with failure message when coupon redemption fails" {
        // Given: A coupon ID and the coupon service returns false indicating no remaining coupons.
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: Calling the redeemCoupon endpoint.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BadRequest with the correct failure message.
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon is created successfully" {
        // Given: A coupon creation request.
        val couponCreate = CouponCreate("Test Coupon", 100, 10L)
        every { couponService.create(couponCreate) } returns Unit // Void in Java translates to Unit in Kotlin

        // When: Calling the createCoupon endpoint.
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK with the correct success message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})