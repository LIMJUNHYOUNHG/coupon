import com.example.coupon.config.DataSourceConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Qualifier


class DataSourceConfigTest : StringSpec({

    "Test dataSource bean creation" {
        // Given: A mock HikariConfig object
        val mockHikariConfig = mockk<HikariConfig>()
        val dataSourceConfig = DataSourceConfig()

        // When: Creating a HikariDataSource using the dataSource bean method
        val dataSource: HikariDataSource = dataSourceConfig.dataSource(mockHikariConfig)


        // Then: The HikariDataSource should be created successfully without exceptions.  We will verify
        // the type and the HikariConfig is properly used.
        dataSource::class.java shouldBe HikariDataSource::class.java
        dataSource.hikariConfigMXBean shouldBe mockHikariConfig

    }

    "Test hikariConfig bean creation" {
        // Given: An instance of DataSourceConfig
        val dataSourceConfig = DataSourceConfig()

        // When: Creating a HikariConfig using the hikariConfig bean method
        val hikariConfig: HikariConfig = dataSourceConfig.hikariConfig()

        // Then: The HikariConfig should be created successfully.
        hikariConfig::class.java shouldBe HikariConfig::class.java
    }
})

// src/main/kotlin/com/example/coupon/controller/CouponController.kt
import com.example.coupon.controller.CouponController
import com.example.coupon.controller.CouponCreate
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "Test redeemCoupon endpoint - success" {
        // Given: A couponId and the couponService returns true for successful redemption.
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: Calling the redeemCoupon endpoint.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK and contain the success message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "Test redeemCoupon endpoint - failure" {
        // Given: A couponId and the couponService returns false for unsuccessful redemption.
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: Calling the redeemCoupon endpoint.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BAD_REQUEST and contain the failure message.
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "Test createCoupon endpoint" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 10)
        every { couponService.create(couponCreate) } returns Unit

        // When: Calling the createCoupon endpoint.
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK and contain the success message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})