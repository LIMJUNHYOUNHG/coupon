class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon - returns success message when coupon is redeemed" {
        // Given: couponService.redeemCoupon returns true
        val couponId: Long = 1
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon is called
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: response should be OK with success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon - returns bad request when coupon is not available" {
        // Given: couponService.redeemCoupon returns false
        val couponId: Long = 1
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon is called
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: response should be BAD_REQUEST with appropriate message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon - returns success message when coupon is created" {
        // Given: CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 100, 10L)
        every { couponService.create(couponCreate) } returns Unit

        // When: createCoupon is called
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: response should be OK with success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})