import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class CouponTest : BehaviorSpec({
    // Given: sample data for Coupon
    val couponName = "TestCoupon"
    val couponCount: Long = 100

    Given("A Coupon with name '$couponName' and count '$couponCount'") {
        // When: Creating a Coupon instance using the builder
        val coupon = com.example.coupon.dao.Coupon.builder()
            .name(couponName)
            .count(couponCount)
            .build()

        Then("The Coupon's name should be '$couponName'") {
            coupon.name shouldBe couponName
        }

        Then("The Coupon's count should be '$couponCount'") {
            coupon.count shouldBe couponCount
        }
    }
})