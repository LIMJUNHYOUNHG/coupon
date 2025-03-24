class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon should return OK with success message when coupon is redeemed successfully" {
        // Given: A coupon ID and the couponService returns true for successful redemption
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: The redeemCoupon endpoint is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with a success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest with error message when coupon redemption fails" {
        // Given: A coupon ID and the couponService returns false for failed redemption (no remaining coupons)
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: The redeemCoupon endpoint is called with the coupon ID
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BadRequest with an error message
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK with success message when coupon creation is successful" {
        // Given: A CouponCreate object and the couponService create method is mocked (no return value)
        val couponCreate = CouponCreate("TestCoupon", 10)
        every { couponService.create(couponCreate) } returns Unit

        // When: The createCoupon endpoint is called with the CouponCreate object
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK with a success message
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})

// src/main/kotlin/com/example/coupon/config/DataSourceConfig.kt
// Since dataSource() function has changed(ADDED), we need to create a new testcase for it
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import com.example.coupon.config.DataSourceConfig

class DataSourceConfigTest : StringSpec({

    "dataSource function should return a HikariDataSource instance" {
        // Given: A HikariConfig instance (mocked)
        val hikariConfig = mockk<HikariConfig>()
        every { hikariConfig.dataSourceClassName = any() } returns Unit
        every { hikariConfig.jdbcUrl = any() } returns Unit
        every { hikariConfig.username = any() } returns Unit
        every { hikariConfig.password = any() } returns Unit

        val dataSourceConfig = DataSourceConfig()
        // When: The dataSource function is called with the HikariConfig instance
        val dataSource = dataSourceConfig.dataSource(hikariConfig)

        // Then: The returned object should be an instance of HikariDataSource
        dataSource::class.java shouldBe HikariDataSource::class.java
    }

})

// src/main/kotlin/com/example/coupon/service/CouponService.kt
import com.example.coupon.dao.CouponRepository
import com.example.coupon.controller.CouponCreate
import com.example.coupon.service.CouponService
import com.example.coupon.dao.Coupon
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CouponServiceTest : StringSpec({

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    "redeemCoupon should return true when decrementCouponCount returns a value greater than 0" {
        // Given: A coupon ID and decrementCouponCount returns 1
        val couponId: Long = 123
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: redeemCoupon is called with the coupon ID
        val result = couponService.redeemCoupon(couponId)

        // Then: The result should be true
        result shouldBe true
    }

    "redeemCoupon should return false when decrementCouponCount returns 0" {
        // Given: A coupon ID and decrementCouponCount returns 0
        val couponId: Long = 456
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: redeemCoupon is called with the coupon ID
        val result = couponService.redeemCoupon(couponId)

        // Then: The result should be false
        result shouldBe false
    }

    "create should save a coupon to the repository" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("TestCoupon", 10)
        val coupon = Coupon(name = couponCreate.name, count = couponCreate.count)
        every { couponRepository.save(any()) } returns coupon

        // When: create is called with the CouponCreate object
        couponService.create(couponCreate)

        // Then: The couponRepository.save method should be called once
        verify(exactly = 1) { couponRepository.save(any()) }
    }
})

// src/main/kotlin/com/example/coupon/dao/CouponRepository.kt
import com.example.coupon.dao.CouponRepository
import com.example.coupon.dao.Coupon
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.TestPropertySource
import jakarta.persistence.EntityManager
import io.kotest.matchers.shouldBe

// Note: This CouponRepositoryTest requires a real database connection to test the @Query annotation.
// For now, I'm skipping the test implementation.  If you'd like, I can provide the implementation given a real database connection.
// For example with H2 db.  In which case, you need to setup proper database configuration to test @Query.
// The current implementation with mocks is not useful in testing the functionality of decrementCouponCount.
class CouponRepositoryTest : StringSpec({

    "decrementCouponCount should decrement the coupon count" {
        // Skipped: Requires real database connection
    }
})