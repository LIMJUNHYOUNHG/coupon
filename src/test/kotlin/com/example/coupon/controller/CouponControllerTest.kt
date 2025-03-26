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

    "redeemCoupon should return OK if coupon redemption is successful" {
        // Given: A coupon ID and a successful redemption from the service
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: Calling the redeemCoupon method on the controller
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with the correct message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest if coupon redemption fails" {
        // Given: A coupon ID and a failed redemption from the service
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: Calling the redeemCoupon method on the controller
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BadRequest with the correct message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message" {
        // Given: A CouponCreate object and the service's create method is called
        val couponCreate = CouponCreate("TestCoupon", 100, 5L)
        every { couponService.create(couponCreate) } returns Unit // Mock void method

        // When: Calling the createCoupon method on the controller
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK with the correct message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})