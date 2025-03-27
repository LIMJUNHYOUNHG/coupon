import com.example.coupon.controller.CouponController
import com.example.coupon.controller.CouponCreate
import com.example.coupon.service.BuyService
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : DescribeSpec() {

    override fun extensions() = listOf(SpringExtension())

    private val couponService: CouponService = mockk()
    private val buyService: BuyService = mockk()
    private val couponController = CouponController(couponService, buyService)

    init {
        describe("CouponController") {
            describe("redeemCoupon") {
                it("should return OK when coupon redemption is successful") {
                    // Given: CouponService.redeemCoupon returns true (success)
                    val couponId: Long = 123
                    every { couponService.redeemCoupon(couponId) } returns true

                    // When: redeemCoupon is called
                    val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

                    // Then: the response should be OK with the success message.
                    response.statusCode shouldBe HttpStatus.OK
                    response.body shouldBe "쿠폰 사용 성공"

                    // Verify that redeemCoupon was called with the correct couponId.
                    verify { couponService.redeemCoupon(couponId) }
                }

                it("should return BadRequest when coupon redemption fails (no remaining coupons)") {
                    // Given: CouponService.redeemCoupon returns false (failure)
                    val couponId: Long = 456
                    every { couponService.redeemCoupon(couponId) } returns false

                    // When: redeemCoupon is called
                    val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

                    // Then: the response should be BadRequest with the failure message.
                    response.statusCode shouldBe HttpStatus.BAD_REQUEST
                    response.body shouldBe "잔여 쿠폰 없음"

                    // Verify that redeemCoupon was called with the correct couponId.
                    verify { couponService.redeemCoupon(couponId) }
                }
            }

            describe("createCoupon") {
                it("should return OK when coupon creation is successful") {
                    // Given: CouponService.create is called (we mock the creation)
                    val couponCreate = CouponCreate("test", 10)
                    every { couponService.create(couponCreate) } returns Unit

                    // When: createCoupon is called
                    val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

                    // Then: the response should be OK with the success message.
                    response.statusCode shouldBe HttpStatus.OK
                    response.body shouldBe "쿠폰 생성 성공"

                    // Verify that create was called.
                    verify { couponService.create(couponCreate) }
                }
            }

            describe("buyCoupon") {
                it("should return OK when buy coupon is successful") {
                    // Given: BuyService.buy is called (we mock the buy)
                    val productId: Long = 1L
                    every { buyService.buy(productId) } returns Unit

                    // When: buyCoupon is called
                    val response: ResponseEntity<String> = couponController.buyCoupon(productId)

                    // Then: the response should be OK with the ok message.
                    response.statusCode shouldBe HttpStatus.OK
                    response.body shouldBe "ok"

                    // Verify that buy was called.
                    verify { buyService.buy(productId) }
                }
            }
        }
    }
}