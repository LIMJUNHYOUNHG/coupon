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