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

    "Test redeemCoupon endpoint - success" {
        // Given: A couponId and the couponService returns true for successful redemption.
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: Calling the redeemCoupon endpoint.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK and contain the success message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "Test redeemCoupon endpoint - failure" {
        // Given: A couponId and the couponService returns false for unsuccessful redemption.
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: Calling the redeemCoupon endpoint.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BAD_REQUEST and contain the failure message.
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "Test createCoupon endpoint" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 10)
        every { couponService.create(couponCreate) } returns Unit

        // When: Calling the createCoupon endpoint.
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK and contain the success message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})