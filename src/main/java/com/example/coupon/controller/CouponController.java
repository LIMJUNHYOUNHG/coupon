class CouponControllerTest : BehaviorSpec({

    val couponService = mockk<com.example.coupon.service.CouponService>()
    val couponController = com.example.coupon.controller.CouponController(couponService)

    given("A coupon ID") {
        val couponId: Long = 123L

        and("The coupon is available for redemption") {
            every { couponService.redeemCoupon(couponId) } returns true

            `when`("redeemCoupon is called") {
                val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

                then("The response should be OK with a success message") {
                    response.statusCode shouldBe HttpStatus.OK
                    response.body shouldBe "쿠폰 사용 성공"
                }
            }
        }

        and("The coupon is not available for redemption") {
            every { couponService.redeemCoupon(couponId) } returns false

            `when`("redeemCoupon is called") {
                val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

                then("The response should be BAD REQUEST with an error message") {
                    response.statusCode shouldBe HttpStatus.BAD_REQUEST
                    response.body shouldBe "잔여 쿠폰 없음"
                }
            }
        }
    }

    given("Valid coupon creation details") {
        val couponCreate = com.example.coupon.controller.CouponCreate("Test Coupon", 100, 1000L)

        every { couponService.create(couponCreate) } returns Unit

        `when`("createCoupon is called") {
            val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

            then("The response should be OK with a success message") {
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe "쿠폰 생성 성공"
            }
        }
    }
})