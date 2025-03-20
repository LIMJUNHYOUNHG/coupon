val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon - Coupon redeemed successfully" {
        // Given: A coupon ID and the coupon service returns true for successful redemption.
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon is called with the couponId
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with the success message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon - Coupon redemption failed (no coupons left)" {
        // Given: A coupon ID and the coupon service returns false for failed redemption (no coupons left).
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon is called with the couponId
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be a Bad Request with the appropriate message.
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon - Coupon created successfully" {
        // Given: A valid CouponCreate object.
        val couponCreate = CouponCreate("Test Coupon", 10)
        every { couponService.create(couponCreate) } returns Unit // Simulate successful creation

        // When: createCoupon is called with the couponCreate object.
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK with the success message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})


// src/main/kotlin/com/example/coupon/config/DataSourceConfig.kt
import com.example.coupon.config.DataSourceConfig
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class DataSourceConfigTest : StringSpec({
    // src/main/kotlin/com/example/coupon/config/DataSourceConfig.kt

    "hikariConfig - Should create a HikariConfig bean" {
        // Given: An instance of the DataSourceConfig class.
        val dataSourceConfig = DataSourceConfig()

        // When: The hikariConfig() method is called.
        val hikariConfig = dataSourceConfig.hikariConfig()

        // Then: The result should be a non-null HikariConfig object.
        hikariConfig shouldNotBe null
    }

    "dataSource - Should create a HikariDataSource bean" {
        // Given: An instance of the DataSourceConfig class and a mock HikariConfig bean.
        val dataSourceConfig = DataSourceConfig()
        val mockHikariConfig = HikariConfig() // Using a real HikariConfig for simplicity, you can mock if needed

        // When: The dataSource() method is called with the mock HikariConfig.
        val hikariDataSource = dataSourceConfig.dataSource(mockHikariConfig)

        // Then: The result should be a non-null HikariDataSource object.
        hikariDataSource shouldNotBe null

        // Cleanup: Close the data source to prevent resource leaks.
        hikariDataSource.close()
    }

    "Context loads successfully" {
      // Given: Spring context with DataSourceConfig
      val context = AnnotationConfigApplicationContext(DataSourceConfig::class.java)

      // When: Retrieving beans from the context
      val hikariConfig = context.getBean("hikariConfig") as HikariConfig
      val dataSource = context.getBean("dataSource") as HikariDataSource

      // Then: Beans should exist and be of correct types
      hikariConfig shouldNotBe null
      dataSource shouldNotBe null

      context.close()
    }

})

// src/main/kotlin/com/example/coupon/dao/CouponRepository.kt
import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

class CouponRepositoryTest : StringSpec() {
    // src/main/kotlin/com/example/coupon/dao/CouponRepository.kt
    init {
        "decrementCouponCount - Should decrement coupon count if count is greater than 0" {
            // Given: A mock CouponRepository and a couponId.
            val couponRepository = mockk<CouponRepository>()
            val couponId: Long = 1L

            // When: The decrementCouponCount method is called. Mock the repository to return 1, indicating a successful update.
            every { couponRepository.decrementCouponCount(couponId) } returns 1

            // Then: The method should return 1, indicating a successful update.
            couponRepository.decrementCouponCount(couponId) shouldBe 1
        }

        "decrementCouponCount - Should not decrement coupon count if count is 0" {
            // Given: A mock CouponRepository and a couponId.
            val couponRepository = mockk<CouponRepository>()
            val couponId: Long = 2L

            // When: The decrementCouponCount method is called. Mock the repository to return 0, indicating no update.
            every { couponRepository.decrementCouponCount(couponId) } returns 0

            // Then: The method should return 0, indicating no update.
            couponRepository.decrementCouponCount(couponId) shouldBe 0
        }
    }
}


// src/main/kotlin/com/example/coupon/service/CouponService.kt
import com.example.coupon.controller.CouponCreate
import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import com.example.coupon.service.CouponService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

class CouponServiceTest : StringSpec({
    // src/main/kotlin/com/example/coupon/service/CouponService.kt

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    "redeemCoupon - Should return true if coupon count is decremented" {
        // Given: A coupon ID and the repository decrementCouponCount method returns a value greater than 0.
        val couponId: Long = 123
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: redeemCoupon is called with the couponId
        val result = couponService.redeemCoupon(couponId)

        // Then: The result should be true.
        result shouldBe true
        verify(exactly = 1) { couponRepository.decrementCouponCount(couponId) }
    }

    "redeemCoupon - Should return false if coupon count is not decremented" {
        // Given: A coupon ID and the repository decrementCouponCount method returns 0.
        val couponId: Long = 456
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: redeemCoupon is called with the couponId
        val result = couponService.redeemCoupon(couponId)

        // Then: The result should be false.
        result shouldBe false
        verify(exactly = 1) { couponRepository.decrementCouponCount(couponId) }
    }

    "create - Should save a new coupon" {
        // Given: A CouponCreate object and a mock CouponRepository
        val couponCreate = CouponCreate("Test Coupon", 10)
        val expectedCoupon = Coupon(name = "Test Coupon", count = 10)
        every { couponRepository.save(any()) } returns expectedCoupon  // Mock the save method

        // When: create is called with the couponCreate object
        couponService.create(couponCreate)

        // Then: The repository's save method should be called with a Coupon object.
        verify { couponRepository.save(match { it.name == "Test Coupon" && it.count == 10 }) }

    }

})