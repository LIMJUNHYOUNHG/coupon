import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import com.example.coupon.dao.Coupon

class CouponTest : FunSpec({

    test("Coupon entity should be created correctly") {
        // Given: Coupon name and count
        val name = "Test Coupon"
        val count: Long = 10

        // When: Creating a Coupon instance using the builder
        val coupon = Coupon.builder()
            .name(name)
            .count(count)
            .build()

        // Then: Verify that the coupon's properties are set correctly
        coupon.name shouldBe name
        coupon.count shouldBe count
    }
})