class CouponControllerTest : FunSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    test("redeemCoupon - Successful redemption") {
        // Given: A coupon ID and the coupon service returns true for successful redemption
        val couponId: Long = 123L
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon method is called
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK and the body should contain success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    test("redeemCoupon - Unsuccessful redemption") {
        // Given: A coupon ID and the coupon service returns false for unsuccessful redemption (no remaining coupons)
        val couponId: Long = 456L
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon method is called
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be Bad Request and the body should contain an appropriate error message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    test("createCoupon - Successful coupon creation") {
        // Given: CouponCreate data and the coupon service create method is mocked
        val couponCreate = CouponCreate("TestCoupon", 10, 100L)
        every { couponService.create(couponCreate) } returns Unit // void method is mocked as Unit

        // When: createCoupon method is called
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK and the body should contain success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }

})