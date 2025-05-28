import com.example.coupon.dao.Coupon
import io.kotest.core.spec.style.StringSpec

class CouponTest : StringSpec({
  "Coupon can be instantiated with default constructor" {
    // Given: No initial state (testing default constructor)

    // When: Create an instance of Coupon using the default constructor
    val coupon = Coupon()

    // Then: The coupon object should exist, and its fields should have default values (null/0)
    coupon.id shouldBe null
    coupon.name shouldBe null
    coupon.count shouldBe null
  }
})