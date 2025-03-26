import com.example.coupon.controller.CouponController
import com.example.coupon.controller.CouponCreate
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: A coupon ID and the coupon service returns true (success)
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK and the body should contain the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"

        // Verify that the redeemCoupon method was called on the service with the correct couponId
        verify { couponService.redeemCoupon(couponId) }
    }

    "redeemCoupon should return BadRequest with failure message when coupon redemption fails (no coupons left)" {
        // Given: A coupon ID and the coupon service returns false (failure)
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BadRequest and the body should contain the failure message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"

        // Verify that the redeemCoupon method was called on the service with the correct couponId
        verify { couponService.redeemCoupon(couponId) }
    }

    "createCoupon should return OK with success message when coupon creation is successful" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 1000, 100)
        every { couponService.create(couponCreate) } returns Unit // void method

        // When: createCoupon is called with the CouponCreate object
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK and the body should contain the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"

        // Verify that the create method was called on the service with the correct CouponCreate object
        verify { couponService.create(couponCreate) }
    }
})