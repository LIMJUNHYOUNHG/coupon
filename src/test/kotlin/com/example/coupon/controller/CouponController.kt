class CouponControllerTest : BehaviorSpec({

    given("CouponController and CouponService are mocked") {
        val couponService = mockk<com.example.coupon.service.CouponService>()
        val couponController = com.example.coupon.controller.CouponController(couponService)

        `when`("redeemCoupon is called with existing coupon ID and service returns true") {
            val couponId: Long = 1
            every { couponService.redeemCoupon(couponId) } returns true

            val response = couponController.redeemCoupon(couponId)

            then("the response should be OK with message '쿠폰 사용 성공'") {
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe "쿠폰 사용 성공"
            }
        }

        `when`("redeemCoupon is called with existing coupon ID and service returns false") {
            val couponId: Long = 1
            every { couponService.redeemCoupon(couponId) } returns false

            val response = couponController.redeemCoupon(couponId)

            then("the response should be BAD_REQUEST with message '잔여 쿠폰 없음'") {
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
                response.body shouldBe "잔여 쿠폰 없음"
            }
        }

        `when`("createCoupon is called with valid CouponCreate object") {
            val couponCreate = com.example.coupon.controller.CouponCreate("Test Coupon", 10)
            every { couponService.create(couponCreate) } returns Unit // Assuming create method returns void.

            val response = couponController.createCoupon(couponCreate)

            then("the response should be OK with message '쿠폰 생성 성공'") {
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe "쿠폰 생성 성공"
            }
        }

    }
})