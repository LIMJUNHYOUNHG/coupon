import com.example.coupon.controller.CouponController
import com.example.coupon.controller.CouponCreate
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: A coupon ID and the coupon service successfully redeems the coupon
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: The redeemCoupon endpoint is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest with error message when coupon cannot be redeemed" {
        // Given: A coupon ID and the coupon service fails to redeem the coupon (e.g., no coupons left)
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: The redeemCoupon endpoint is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BadRequest with the error message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon is created successfully" {
        // Given: A CouponCreate object and the coupon service successfully creates the coupon
        val couponCreate = CouponCreate("Test Coupon", 100, 10L)
        every { couponService.create(couponCreate) } returns Unit // void method

        // When: The createCoupon endpoint is called with the CouponCreate object
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK with the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})