import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import com.example.coupon.dao.CouponRepository
import com.example.coupon.service.CouponService
import com.example.coupon.controller.CouponCreate
import com.example.coupon.dao.Coupon

class CouponServiceTest : StringSpec({

    val couponRepository = mockk<CouponRepository>(relaxed = true) // relaxed = true to avoid specifying behavior for all methods
    val couponService = CouponService(couponRepository)


    "redeemCoupon should return true when decrementCouponCount returns a positive value" {
        // Given:
        val couponId: Long = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When:
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then:
        result shouldBe true
        verify { couponRepository.decrementCouponCount(couponId) }
    }

    "redeemCoupon should return false when decrementCouponCount returns zero" {
        // Given:
        val couponId: Long = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When:
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then:
        result shouldBe false
        verify { couponRepository.decrementCouponCount(couponId) }
    }

    "create should save a coupon to the repository" {
        // Given:
        val couponCreate = CouponCreate(name = "SummerSale", count = 50)

        // When:
        couponService.create(couponCreate)

        // Then:
        verify {
            couponRepository.save(match { coupon ->
                coupon.name == couponCreate.name && coupon.count == couponCreate.count
            })
        }
    }
})