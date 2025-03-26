import com.example.coupon.controller.CouponController
import com.example.coupon.controller.CouponCreate
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon redemption is successful" {
        // Given: CouponService.redeemCoupon returns true
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon is called with a valid couponId
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: ResponseEntity should be OK and contain the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest with error message when coupon redemption fails" {
        // Given: CouponService.redeemCoupon returns false
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon is called with a couponId that cannot be redeemed
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: ResponseEntity should be BadRequest and contain the error message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon creation is successful" {
        // Given: A valid CouponCreate object and CouponService.create is mocked
        val couponCreate = CouponCreate("Test Coupon", 100, 10L)
        every { couponService.create(couponCreate) } returns Unit // void method

        // When: createCoupon is called with valid CouponCreate
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: ResponseEntity should be OK and contain the success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})