import com.example.coupon.controller.CouponController
import com.example.coupon.controller.CouponCreate
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : DescribeSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    describe("CouponController") {

        describe("redeemCoupon") {
            it("should return OK with success message when coupon is redeemed successfully") {
                // Given:
                val couponId: Long = 123
                every { couponService.redeemCoupon(couponId) } returns true

                // When:
                val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

                // Then:
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe "쿠폰 사용 성공"
                verify(exactly = 1) { couponService.redeemCoupon(couponId) }
            }

            it("should return BAD_REQUEST with error message when coupon redemption fails") {
                // Given:
                val couponId: Long = 123
                every { couponService.redeemCoupon(couponId) } returns false

                // When:
                val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

                // Then:
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
                response.body shouldBe "잔여 쿠폰 없음"
                verify(exactly = 1) { couponService.redeemCoupon(couponId) }
            }
        }

        describe("createCoupon") {
            it("should return OK with success message when coupon is created successfully") {
                // Given:
                val couponCreate = CouponCreate("TestCoupon", 10)
                every { couponService.create(couponCreate) } returns Unit

                // When:
                val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

                // Then:
                response.statusCode shouldBe HttpStatus.OK
                response.body shouldBe "쿠폰 생성 성공"
                verify(exactly = 1) { couponService.create(couponCreate) }
            }
        }
    }
})