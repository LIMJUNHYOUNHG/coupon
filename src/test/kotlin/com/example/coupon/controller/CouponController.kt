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

    // Given: Mock the CouponService
    val couponService = mockk<CouponService>()

    // Given: Create a CouponController instance with the mocked service
    val couponController = CouponController(couponService)

    "redeemCoupon should return success message when coupon is redeemed successfully" {
        // Given: A coupon ID and the coupon service returns true (success)
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon is called on the controller
        val responseEntity: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with the success message
        responseEntity.statusCode shouldBe HttpStatus.OK
        responseEntity.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return error message when coupon redemption fails" {
        // Given: A coupon ID and the coupon service returns false (failure)
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon is called on the controller
        val responseEntity: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be a Bad Request with the error message
        responseEntity.statusCode shouldBe HttpStatus.BAD_REQUEST
        responseEntity.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return success message when coupon creation is successful" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 10)
        every { couponService.create(couponCreate) } returns Unit // Simulate void method

        // When: createCoupon is called on the controller
        val responseEntity: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK with the success message
        responseEntity.statusCode shouldBe HttpStatus.OK
        responseEntity.body shouldBe "쿠폰 생성 성공"
    }
})