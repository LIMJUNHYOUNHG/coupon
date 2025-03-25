class CouponControllerTest : StringSpec({

    val couponService = mockk<com.example.coupon.service.CouponService>()
    val couponController = com.example.coupon.controller.CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: couponId and service returns true (successful redemption)
        val couponId = 1L
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon method is called
        val response = couponController.redeemCoupon(couponId)

        // Then: response should be OK with success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest with error message when coupon redemption fails" {
        // Given: couponId and service returns false (failed redemption)
        val couponId = 1L
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon method is called
        val response = couponController.redeemCoupon(couponId)

        // Then: response should be BadRequest with error message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon is created successfully" {
        // Given: CouponCreate object
        val couponCreate = com.example.coupon.controller.CouponCreate("Test Coupon", 100, 10L)
        every { couponService.create(couponCreate) } returns Unit // Assuming create returns void

        // When: createCoupon method is called
        val response = couponController.createCoupon(couponCreate)

        // Then: response should be OK with success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})