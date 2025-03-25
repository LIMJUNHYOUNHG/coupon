class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: Coupon ID and that the redeemCoupon method returns true
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon is called
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: Response should be OK and body should be "쿠폰 사용 성공"
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest with failure message when coupon is not redeemed successfully" {
        // Given: Coupon ID and that the redeemCoupon method returns false
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon is called
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: Response should be BadRequest and body should be "잔여 쿠폰 없음"
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon is created successfully" {
        // Given: CouponCreate object and that the create method executes successfully.
        val couponCreate = CouponCreate("testCoupon", 100, 5L)
        every { couponService.create(couponCreate) } returns Unit // void method mock

        // When: createCoupon is called
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: Response should be OK and body should be "쿠폰 생성 성공"
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})