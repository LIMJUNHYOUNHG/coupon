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
        // Given: A coupon ID and the coupon service returns true for successful redemption
        val couponId = 123L
        every { couponService.redeemCoupon(couponId) } returns true

        // When: Calling the redeemCoupon endpoint
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK and the message should be "쿠폰 사용 성공"
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
        verify(exactly = 1) { couponService.redeemCoupon(couponId) }
    }

    "redeemCoupon should return BadRequest with error message when coupon redemption fails" {
        // Given: A coupon ID and the coupon service returns false for failed redemption
        val couponId = 456L
        every { couponService.redeemCoupon(couponId) } returns false

        // When: Calling the redeemCoupon endpoint
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BadRequest and the message should be "잔여 쿠폰 없음"
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
        verify(exactly = 1) { couponService.redeemCoupon(couponId) }
    }

    "createCoupon should return OK with success message when coupon creation is successful" {
        // Given: A CouponCreate object and the coupon service create method is called
        val couponCreate = CouponCreate("Test Coupon", 100, 10L)
        every { couponService.create(couponCreate) } returns Unit // void method

        // When: Calling the createCoupon endpoint
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK and the message should be "쿠폰 생성 성공"
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
        verify(exactly = 1) { couponService.create(couponCreate) }
    }
})