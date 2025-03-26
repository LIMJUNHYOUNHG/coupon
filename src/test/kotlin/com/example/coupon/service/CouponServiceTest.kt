import com.example.coupon.controller.CouponCreate
import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.dao.OptimisticLockingFailureException

class CouponServiceTest : StringSpec({

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    "redeemCoupon should return true when decrementCouponCount returns 1" {
        // Given: A coupon ID and the coupon repository returns 1 indicating a successful decrement
        val couponId = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: Calling the redeemCoupon method
        val result = couponService.redeemCoupon(couponId)

        // Then: The result should be true
        result shouldBe true
        verify(exactly = 1) { couponRepository.decrementCouponCount(couponId) }
    }

    "redeemCoupon should return false when decrementCouponCount returns 0" {
        // Given: A coupon ID and the coupon repository returns 0 indicating a failed decrement
        val couponId = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: Calling the redeemCoupon method
        val result = couponService.redeemCoupon(couponId)

        // Then: The result should be false
        result shouldBe false
        verify(exactly = 1) { couponRepository.decrementCouponCount(couponId) }
    }

    "create should save a coupon to the repository" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 100, 10L)
        val coupon = Coupon.builder().name(couponCreate.name()).count(couponCreate.count()).build()
        every { couponRepository.save(any<Coupon>()) } returns coupon

        // When: Calling the create method
        couponService.create(couponCreate)

        // Then: The coupon repository's save method should be called with a Coupon object
        verify(exactly = 1) { couponRepository.save(any<Coupon>()) }
    }
})