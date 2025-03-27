import com.example.coupon.controller.CouponCreate
import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.dao.OptimisticLockingFailureException

class CouponServiceTest : FunSpec({

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    test("redeemCoupon should decrement coupon count and return true if successful") {
        // Given:
        val couponId: Long = 1
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When:
        val result = couponService.redeemCoupon(couponId)

        // Then:
        result shouldBe true
        verify { couponRepository.decrementCouponCount(couponId) }
    }

    test("redeemCoupon should return false if decrementing coupon count fails (count is zero)") {
        // Given:
        val couponId: Long = 1
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When:
        val result = couponService.redeemCoupon(couponId)

        // Then:
        result shouldBe false
        verify { couponRepository.decrementCouponCount(couponId) }
    }

    test("create should save a new coupon") {
        // Given:
        val couponCreate = CouponCreate("Test Coupon", 1000, 100L)
        val coupon = Coupon.builder().name(couponCreate.name()).count(couponCreate.count()).build()
        every { couponRepository.save(any<Coupon>()) } returns coupon // Mock the save method

        // When:
        couponService.create(couponCreate)

        // Then:
        verify { couponRepository.save(any<Coupon>()) } // Verify that save was called
    }

})