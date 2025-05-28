import com.example.coupon.controller.CouponController
import com.example.coupon.service.BuyService
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val buyService = mockk<BuyService>()
    val couponController = CouponController(couponService, buyService)

    "buyCoupon should call buyService.buy and return ok" {
        // Given: A productId
        val productId: Long = 123

        // When: Calling buyCoupon with the productId
        every { buyService.buy(productId) } just Runs
        val response: ResponseEntity<String> = couponController.buyCoupon(productId)

        // Then: buyService.buy should be called once with the productId and the response should be ok
        verify(exactly = 1) { buyService.buy(productId) }
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "ok"
    }
})