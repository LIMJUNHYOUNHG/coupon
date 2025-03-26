class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon: Should return OK with success message when coupon is redeemed successfully" {
        // Given: A coupon ID and the coupon service returns true (redeemed successfully)
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon method is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon: Should return BadRequest with failure message when no coupons are left" {
        // Given: A coupon ID and the coupon service returns false (no coupons left)
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon method is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BadRequest with the failure message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon: Should return OK with success message when coupon is created" {
        // Given: A CouponCreate object and the coupon service create method doesn't return anything (void)
        val couponCreate = CouponCreate("Test Coupon", 100, 10L)
        every { couponService.create(couponCreate) } returns Unit

        // When: createCoupon method is called with the CouponCreate object
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK with the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }

})

import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import com.example.coupon.service.CouponService
import com.example.coupon.controller.CouponCreate