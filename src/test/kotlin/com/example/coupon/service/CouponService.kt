import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import io.mockk.verify
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class CouponServiceTest : BehaviorSpec({

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    Given("a couponId") {
        val couponId: Long = 123

        When("redeemCoupon is called and decrementCouponCount returns a value greater than 0") {
            every { couponRepository.decrementCouponCount(couponId) } returns 1

            val result = couponService.redeemCoupon(couponId)

            Then("the result should be true") {
                result shouldBe true
            }
        }

        When("redeemCoupon is called and decrementCouponCount returns 0") {
            every { couponRepository.decrementCouponCount(couponId) } returns 0

            val result = couponService.redeemCoupon(couponId)

            Then("the result should be false") {
                result shouldBe false
            }
        }
    }

    Given("a CouponCreate object") {
        val couponCreate = CouponCreate("Test Coupon", 100, 5L)

        When("create is called") {
            every { couponRepository.save(any<Coupon>()) } returnsArgument 0
            couponService.create(couponCreate)

            Then("a coupon should be saved with the correct values") {
                verify {
                    couponRepository.save(match { coupon ->
                        coupon.name == couponCreate.name && coupon.count == couponCreate.count
                    })
                }
            }
        }
    }
})