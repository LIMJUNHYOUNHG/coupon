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

    // Given: Mock the CouponRepository
    val couponRepository = mockk<CouponRepository>()

    // Given: Create an instance of the CouponService with the mocked repository
    val couponService = CouponService(couponRepository)

    "redeemCoupon should return true when coupon count is decremented successfully" {
        // Given: A coupon ID and the repository returning 1 (meaning updated one row)
        val couponId: Long = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: Calling the redeemCoupon method
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be true
        result shouldBe true
    }

    "redeemCoupon should return false when coupon count is not decremented (count is 0)" {
        // Given: A coupon ID and the repository returning 0 (meaning no rows updated)
        val couponId: Long = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: Calling the redeemCoupon method
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be false
        result shouldBe false
    }

    "create should save a new coupon to the repository" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("SummerDiscount", 50)
        val expectedCoupon = Coupon(name = couponCreate.name(), count = couponCreate.count())

        // When: Calling the create method
        every { couponRepository.save(any()) } returns expectedCoupon

        couponService.create(couponCreate)

        // Then: The repository's save method should be called with the correct coupon object
        verify { couponRepository.save(match { it.name == couponCreate.name() && it.count == couponCreate.count() }) }
    }
})