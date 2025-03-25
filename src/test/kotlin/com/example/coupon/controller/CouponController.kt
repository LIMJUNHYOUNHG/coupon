class CouponControllerTest : BehaviorSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    Given("a couponId and a request") {
        val couponId: Long = 123

        When("redeemCoupon is called and couponService.redeemCoupon returns true") {
            every { couponService.redeemCoupon(couponId) } returns true
            val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

            Then("the response should be OK with the success message") {
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe "쿠폰 사용 성공"
            }
        }

        When("redeemCoupon is called and couponService.redeemCoupon returns false") {
            every { couponService.redeemCoupon(couponId) } returns false
            val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

            Then("the response should be BadRequest with the failure message") {
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
                response.body shouldBe "잔여 쿠폰 없음"
            }
        }
    }

    Given("valid coupon data") {
        val couponCreate = CouponCreate("testCoupon", 1000, 10L)
        every { couponService.create(couponCreate) } returns Unit // void method mock

        When("createCoupon is called") {
            val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

            Then("the response should be OK with success message") {
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe "쿠폰 생성 성공"
            }
        }
    }
})