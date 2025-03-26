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

    "redeemCoupon should return true when decrementCouponCount returns a positive value" {
        // Given: couponRepository.decrementCouponCount returns 1
        val couponId: Long = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: redeemCoupon is called
        val result = couponService.redeemCoupon(couponId)

        // Then: the result should be true
        result shouldBe true
    }

    "redeemCoupon should return false when decrementCouponCount returns 0" {
        // Given: couponRepository.decrementCouponCount returns 0
        val couponId: Long = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: redeemCoupon is called
        val result = couponService.redeemCoupon(couponId)

        // Then: the result should be false
        result shouldBe false
    }

    "create should save a coupon to the repository" {
        // Given: a CouponCreate object
        val couponCreate = CouponCreate("Summer Sale", 50, 100L)
        val coupon = Coupon.builder().name(couponCreate.name()).count(couponCreate.count()).build()
        every { couponRepository.save(any<Coupon>()) } returns coupon // Mock the save method

        // When: create is called
        couponService.create(couponCreate)

        // Then: couponRepository.save should be called with the correct coupon
        verify { couponRepository.save(match { it.name == couponCreate.name() && it.count == couponCreate.count() }) }
    }

    "create should handle exceptions during coupon creation" {
        // Given: a CouponCreate object that will cause a DataIntegrityViolationException
        val couponCreate = CouponCreate("Invalid Coupon", 0, -10L)
        every { couponRepository.save(any<Coupon>()) } throws DataIntegrityViolationException("Invalid data")

        // When: create is called
        // Then: a DataIntegrityViolationException should be thrown
        shouldThrow<DataIntegrityViolationException> {
            couponService.create(couponCreate)
        }
    }
})