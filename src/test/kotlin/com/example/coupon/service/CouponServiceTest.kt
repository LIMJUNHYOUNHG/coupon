import com.example.coupon.controller.CouponCreate
import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

class CouponServiceTest : StringSpec({

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    "redeemCoupon should return true if decrementCouponCount returns a value greater than 0" {
        // Given: A coupon ID and the decrementCouponCount method returns 1
        val couponId: Long = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: Calling the redeemCoupon method
        val result = couponService.redeemCoupon(couponId)

        // Then: The result should be true
        result shouldBe true
    }

    "redeemCoupon should return false if decrementCouponCount returns 0" {
        // Given: A coupon ID and the decrementCouponCount method returns 0
        val couponId: Long = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: Calling the redeemCoupon method
        val result = couponService.redeemCoupon(couponId)

        // Then: The result should be false
        result shouldBe false
    }

    "create should save a coupon to the repository" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 100, 10L)
        val expectedCoupon = Coupon.builder().name(couponCreate.name()).count(couponCreate.count()).build()

        every { couponRepository.save(any<Coupon>()) } returns expectedCoupon // Mock the save method

        // When: Calling the create method
        couponService.create(couponCreate)

        // Then: The couponRepository.save method should be called once with correct parameters
        verify(exactly = 1) { couponRepository.save(any<Coupon>()) } // Check if save was called exactly once
    }
})