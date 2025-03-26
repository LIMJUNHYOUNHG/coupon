class CouponControllerTest : FunSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    test("redeemCoupon - when coupon redemption is successful, return OK") {
        // Given:
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When:
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then:
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    test("redeemCoupon - when coupon redemption fails due to no remaining coupons, return BadRequest") {
        // Given:
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When:
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then:
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    test("createCoupon - when coupon creation is successful, return OK") {
        // Given:
        val couponCreate = CouponCreate("test coupon", 100, 5)
        every { couponService.create(couponCreate) } returns Unit // Mock void return

        // When:
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then:
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }

})