class CouponControllerTest : StringSpec({

    val couponService = mock(CouponService::class.java)
    val couponController = CouponController(couponService)

    "redeemCoupon should return OK if coupon is redeemed successfully" {
        // Given: A coupon ID and the coupon service returns true for successful redemption
        val couponId: Long = 123L
        `when`(couponService.redeemCoupon(couponId)).thenReturn(true)

        // When: redeemCoupon is called with the given coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response status should be OK and the body should contain "쿠폰 사용 성공"
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"

        verify(couponService, times(1)).redeemCoupon(couponId)
    }

    "redeemCoupon should return BadRequest if coupon redemption fails" {
        // Given: A coupon ID and the coupon service returns false indicating no available coupons
        val couponId: Long = 456L
        `when`(couponService.redeemCoupon(couponId)).thenReturn(false)

        // When: redeemCoupon is called with the given coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response status should be BadRequest and the body should contain "잔여 쿠폰 없음"
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"

        verify(couponService, times(1)).redeemCoupon(couponId)
    }

    "createCoupon should return OK and success message when coupon creation is successful" {
        // Given: a CouponCreate object representing the coupon to be created
        val couponCreate = CouponCreate("TestCoupon", 10)

        // When: createCoupon is called with the CouponCreate object
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response status should be OK and the body should contain "쿠폰 생성 성공"
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"

        // Verify that couponService.create was called with the correct couponCreate object
        verify(couponService, times(1)).create(couponCreate)
    }
})

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.kotest.matchers.shouldBe

// src/main/kotlin/com/example/coupon/config/DataSourceConfig.kt
class DataSourceConfigTest : StringSpec() {

    "dataSource should return a HikariDataSource" {
        // Given: A mocked HikariConfig
        val hikariConfig = mockk<HikariConfig>()
        val dataSourceConfig = com.example.coupon.config.DataSourceConfig()

        // When: dataSource is called with the HikariConfig
        val dataSource = dataSourceConfig.dataSource(hikariConfig)

        // Then: The result should be an instance of HikariDataSource
        dataSource::class.java shouldBe HikariDataSource::class.java
    }
}