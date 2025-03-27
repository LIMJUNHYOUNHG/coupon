import com.example.coupon.controller.CouponController
import com.example.coupon.controller.CouponCreate
import com.example.coupon.service.BuyService
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.mockito.Mockito
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : DescribeSpec() {

    override fun extensions() = listOf(SpringExtension)

    init {
        val couponService = Mockito.mock(CouponService::class.java)
        val buyService = Mockito.mock(BuyService::class.java)
        val couponController = CouponController(couponService, buyService)

        describe("CouponController") {
            describe("redeemCoupon") {
                it("should return OK when coupon redemption is successful") {
                    // Given: A coupon ID and the couponService returns true (success)
                    val couponId: Long = 123
                    Mockito.`when`(couponService.redeemCoupon(couponId)).thenReturn(true)

                    // When: The redeemCoupon endpoint is called
                    val responseEntity: ResponseEntity<String> = couponController.redeemCoupon(couponId)

                    // Then: The response should be OK and the body should indicate success
                    responseEntity.statusCode shouldBe HttpStatus.OK
                    responseEntity.body shouldBe "쿠폰 사용 성공"
                }

                it("should return BadRequest when no coupons are left") {
                    // Given: A coupon ID and the couponService returns false (no coupons)
                    val couponId: Long = 456
                    Mockito.`when`(couponService.redeemCoupon(couponId)).thenReturn(false)

                    // When: The redeemCoupon endpoint is called
                    val responseEntity: ResponseEntity<String> = couponController.redeemCoupon(couponId)

                    // Then: The response should be BadRequest and the body should indicate failure
                    responseEntity.statusCode shouldBe HttpStatus.BAD_REQUEST
                    responseEntity.body shouldBe "잔여 쿠폰 없음"
                }
            }

            describe("createCoupon") {
                it("should return OK when coupon creation is successful") {
                    // Given: A CouponCreate object
                    val couponCreate = CouponCreate("test", 10)

                    // When: The createCoupon endpoint is called
                    val responseEntity: ResponseEntity<String> = couponController.createCoupon(couponCreate)

                    // Then: The response should be OK and the body should indicate success
                    responseEntity.statusCode shouldBe HttpStatus.OK
                    responseEntity.body shouldBe "쿠폰 생성 성공"
                }
            }

             describe("buyCoupon") {
                it("should return OK when buy coupon is successful") {
                    // Given: A product Id
                    val productId: Long = 789

                    // When: The buyCoupon endpoint is called
                    val responseEntity: ResponseEntity<String> = couponController.buyCoupon(productId)

                    // Then: The response should be OK
                    responseEntity.statusCode shouldBe HttpStatus.OK
                    responseEntity.body shouldBe "ok"
                }
            }
        }
    }
}