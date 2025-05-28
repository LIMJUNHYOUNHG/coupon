import com.example.coupon.controller.CouponController
import com.example.coupon.service.BuyService
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>(relaxed = true)
    val buyService = mockk<BuyService>(relaxed = true)
    val couponController = CouponController(couponService, buyService)

    "buyCoupon should call buyService.buy and return OK" {
        // Given: a product ID
        val productId: Long = 123L

        // When: buyCoupon is called with the product ID
        val response: ResponseEntity<String> = couponController.buyCoupon(productId)

        // Then: buyService.buy should be called with the product ID, and the response should be OK
        verify { buyService.buy(productId) }
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "ok"
    }
})