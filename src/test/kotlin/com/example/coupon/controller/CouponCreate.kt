class CouponCreateTest : FunSpec({
    test("CouponCreate record can be instantiated") {
        // Given:
        val name = "TestCoupon"
        val price = 100
        val count = 10L

        // When:
        val couponCreate = CouponCreate(name, price, count)

        // Then:
        couponCreate.name shouldBe name
        couponCreate.price shouldBe price
        couponCreate.count shouldBe count
    }
})