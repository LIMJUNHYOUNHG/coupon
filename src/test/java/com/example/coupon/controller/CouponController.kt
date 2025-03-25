class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: a coupon ID and the service returns true (success)
        val couponId: Long = 123L
        every { couponService.redeemCoupon(couponId) } returns true

        // When: the redeemCoupon endpoint is called
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: the response should be OK with the correct message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest with failure message when coupon redemption fails" {
        // Given: a coupon ID and the service returns false (failure - no coupons left)
        val couponId: Long = 456L
        every { couponService.redeemCoupon(couponId) } returns false

        // When: the redeemCoupon endpoint is called
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: the response should be BadRequest with the correct message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon is created" {
        // Given: a CouponCreate object and the service create method is called
        val couponCreate = CouponCreate("TestCoupon", 10, 100L)
        every { couponService.create(couponCreate) } returns Unit // void method

        // When: the createCoupon endpoint is called
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: the response should be OK with the correct message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }

})