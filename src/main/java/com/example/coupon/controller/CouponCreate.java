class CouponCreateTest : BehaviorSpec({

    given("a CouponCreate record") {
        val name = "TestCoupon"
        val price = 100
        val count = 1000L

        `when`("the record is created") {
            val couponCreate = com.example.coupon.controller.CouponCreate(name, price, count)

            then("the values should be correctly set") {
                couponCreate.name shouldBe name
                couponCreate.price shouldBe price
                couponCreate.count shouldBe count
            }
        }
    }
})