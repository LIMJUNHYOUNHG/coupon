import com.example.coupon.controller.CouponController
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : DescribeSpec({

    // Given: Mock the CouponService
    val couponService = mockk<CouponService>()

    // Given: Create a CouponController instance with the mocked service
    val couponController = CouponController(couponService)

    describe("CouponController") {
        describe("redeemCoupon") {
            context("when the coupon is successfully redeemed") {
                // Given: The coupon service returns true, indicating successful redemption
                every { couponService.redeemCoupon(1L) } returns true

                // When: The redeemCoupon endpoint is called
                val responseEntity: ResponseEntity<String> = couponController.redeemCoupon(1L)

                // Then: The response status should be OK
                it("should return an OK status") {
                    responseEntity.statusCode shouldBe HttpStatus.OK
                }

                // Then: The response body should indicate successful coupon redemption
                it("should return a success message") {
                    responseEntity.body shouldBe "쿠폰 사용 성공"
                }
            }

            context("when the coupon cannot be redeemed (no remaining coupons)") {
                // Given: The coupon service returns false, indicating unsuccessful redemption
                every { couponService.redeemCoupon(1L) } returns false

                // When: The redeemCoupon endpoint is called
                val responseEntity: ResponseEntity<String> = couponController.redeemCoupon(1L)

                // Then: The response status should be BAD_REQUEST
                it("should return a BAD_REQUEST status") {
                    responseEntity.statusCode shouldBe HttpStatus.BAD_REQUEST
                }

                // Then: The response body should indicate no remaining coupons
                it("should return an error message") {
                    responseEntity.body shouldBe "잔여 쿠폰 없음"
                }
            }
        }

        describe("createCoupon") {
            context("when a coupon is created") {
                // Given: A CouponCreate object is created
                val couponCreate = mockk<com.example.coupon.controller.CouponCreate>()

                // Given: The create method of the coupon service is invoked
                every { couponService.create(couponCreate) } returns Unit

                // When: The createCoupon endpoint is called
                val responseEntity: ResponseEntity<String> = couponController.createCoupon(couponCreate)

                // Then: The response status should be OK
                it("should return an OK status") {
                    responseEntity.statusCode shouldBe HttpStatus.OK
                }

                // Then: The response body should indicate successful coupon creation
                it("should return a success message") {
                    responseEntity.body shouldBe "쿠폰 생성 성공"
                }
            }
        }
    }
})