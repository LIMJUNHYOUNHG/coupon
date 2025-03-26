import com.example.coupon.controller.CouponCreate
import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.dao.DataIntegrityViolationException

class CouponServiceTest : StringSpec({

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    "redeemCoupon should return true if coupon count is decremented successfully" {
        // Given: A coupon ID and the coupon repository decrementing the count successfully (returning 1)
        val couponId: Long = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: Calling the redeemCoupon method
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be true
        result shouldBe true
    }

    "redeemCoupon should return false if coupon count is not decremented (no coupons left)" {
        // Given: A coupon ID and the coupon repository not decrementing the count (returning 0)
        val couponId: Long = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: Calling the redeemCoupon method
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be false
        result shouldBe false
    }

    "create should save a new coupon to the repository" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("New Coupon", 500, 5L)
        val coupon = Coupon.builder().name(couponCreate.name()).count(couponCreate.count()).build()

        every { couponRepository.save(any<Coupon>()) } returns coupon

        // When: Calling the create method
        couponService.create(couponCreate)

        // Then: The coupon repository's save method should be called with a Coupon object.
        verify { couponRepository.save(any<Coupon>()) }
    }

})