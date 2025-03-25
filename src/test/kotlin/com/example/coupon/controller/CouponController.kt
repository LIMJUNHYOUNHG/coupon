class CouponControllerTest : StringSpec({

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: a mock CouponService that returns true for redeemCoupon
        val couponService = mockk<CouponService>()
        every { couponService.redeemCoupon(1L) } returns true
        val couponController = CouponController(couponService)

        // When: calling redeemCoupon with couponId 1
        val response: ResponseEntity<String> = couponController.redeemCoupon(1L)

        // Then: the response should be OK with the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BAD_REQUEST with error message when coupon redemption fails" {
        // Given: a mock CouponService that returns false for redeemCoupon
        val couponService = mockk<CouponService>()
        every { couponService.redeemCoupon(1L) } returns false
        val couponController = CouponController(couponService)

        // When: calling redeemCoupon with couponId 1
        val response: ResponseEntity<String> = couponController.redeemCoupon(1L)

        // Then: the response should be BAD_REQUEST with the error message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon creation is successful" {
        // Given: a mock CouponService and a CouponCreate object
        val couponService = mockk<CouponService>()
        val couponCreate = mockk<com.example.coupon.controller.CouponCreate>()

        every { couponService.create(couponCreate) } returns Unit // void method
        val couponController = CouponController(couponService)

        // When: calling createCoupon with a CouponCreate object
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: the response should be OK with the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})