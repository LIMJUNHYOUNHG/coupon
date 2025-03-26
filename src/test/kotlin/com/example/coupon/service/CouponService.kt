import com.example.coupon.controller.CouponCreate
import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CouponServiceTest : StringSpec({

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    "redeemCoupon should return true when decrementCouponCount returns a positive value" {
        // Given: A coupon ID and the coupon repository returns 1 (meaning count was decremented).
        val couponId: Long = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: Calling the redeemCoupon method of the service with the coupon ID.
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be true.
        result shouldBe true
    }

    "redeemCoupon should return false when decrementCouponCount returns zero" {
        // Given: A coupon ID and the coupon repository returns 0 (meaning no coupon was decremented).
        val couponId: Long = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: Calling the redeemCoupon method of the service with the coupon ID.
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be false.
        result shouldBe false
    }

    "create should save a coupon to the repository" {
        // Given: A CouponCreate object.
        val couponCreate = CouponCreate("TestCoupon", 100, 5L)
        val coupon = Coupon.builder()
            .name(couponCreate.name())
            .count(couponCreate.count())
            .build()

        every { couponRepository.save(any<Coupon>()) } returns coupon

        // When: Calling the create method of the service with the couponCreate object.
        couponService.create(couponCreate)

        // Then: The coupon repository's save method should be called with a Coupon object created from the CouponCreate object.
        verify { couponRepository.save(match {
            it.name == couponCreate.name() && it.count == couponCreate.count()
        }) }
    }
})