class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon - Success" {
        // Given: A couponId and the couponService returns true for redeemCoupon
        val couponId: Long = 123L
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon method is called with the couponId
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon - Failure" {
        // Given: A couponId and the couponService returns false for redeemCoupon
        val couponId: Long = 456L
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon method is called with the couponId
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BAD_REQUEST with the failure message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }
})