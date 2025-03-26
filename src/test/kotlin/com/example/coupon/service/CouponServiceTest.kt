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

    "redeemCoupon should return true if decrementCouponCount returns a positive value" {
        // Given: A coupon ID and decrementCouponCount returns 1
        val couponId: Long = 123
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: redeemCoupon is called with the coupon ID
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be true
        result shouldBe true

        // Verify that the decrementCouponCount method was called on the repository with the correct couponId
        verify { couponRepository.decrementCouponCount(couponId) }
    }

    "redeemCoupon should return false if decrementCouponCount returns 0" {
        // Given: A coupon ID and decrementCouponCount returns 0
        val couponId: Long = 456
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: redeemCoupon is called with the coupon ID
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be false
        result shouldBe false

        // Verify that the decrementCouponCount method was called on the repository with the correct couponId
        verify { couponRepository.decrementCouponCount(couponId) }
    }

    "create should save a new coupon to the repository" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 1000, 100)
        val coupon = Coupon.builder()
            .name(couponCreate.name())
            .count(couponCreate.count())
            .build()
        every { couponRepository.save(any()) } returns coupon // Return the created coupon

        // When: create is called with the CouponCreate object
        couponService.create(couponCreate)

        // Then: The save method should be called on the repository with the correct coupon object
        verify { couponRepository.save(match {
            it.name == couponCreate.name() && it.count == couponCreate.count()
        }) }
    }
})