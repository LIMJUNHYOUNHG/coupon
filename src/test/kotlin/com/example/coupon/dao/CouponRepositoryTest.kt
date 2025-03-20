package com.example.coupon.dao

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CouponRepositoryTest(
    @Autowired private val couponRepository: CouponRepository
) : StringSpec({

    "decrementCouponCount should decrement the count of a coupon if count is greater than 0" {
        // Given: A coupon with an initial count of 5
        val coupon = Coupon(name = "TestCoupon", count = 5)
        couponRepository.save(coupon)
        val couponId = coupon.id

        // When: decrementCouponCount is called for that coupon
        val updatedCount = couponRepository.decrementCouponCount(couponId!!)

        // Then:
        //  - The updatedCount should be 1 (indicating a successful decrement)
        //  - The coupon's count in the database should be 4
        updatedCount shouldBe 1
        val updatedCoupon = couponRepository.findById(couponId).get()
        updatedCoupon.count shouldBe 4
    }

    "decrementCouponCount should not decrement the count of a coupon if count is 0" {
        // Given: A coupon with an initial count of 0
        val coupon = Coupon(name = "TestCoupon", count = 0)
        couponRepository.save(coupon)
        val couponId = coupon.id

        // When: decrementCouponCount is called for that coupon
        val updatedCount = couponRepository.decrementCouponCount(couponId!!)

        // Then:
        //  - The updatedCount should be 0 (indicating no decrement occurred)
        //  - The coupon's count in the database should remain 0
        updatedCount shouldBe 0
        val updatedCoupon = couponRepository.findById(couponId).get()
        updatedCoupon.count shouldBe 0
    }
})

// src/main/kotlin/com/example/coupon/service/CouponServiceTest.kt
package com.example.coupon.service

import com.example.coupon.controller.CouponCreate
import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.dao.OptimisticLockingFailureException

class CouponServiceTest : StringSpec({
    // src/main/kotlin/com/example/coupon/service/CouponService.kt

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    "redeemCoupon should return true if decrementCouponCount returns 1" {
        // Given: a coupon ID and the couponRepository.decrementCouponCount returns 1
        val couponId: Long = 123
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: redeemCoupon is called with the couponId
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: the result should be true
        result shouldBe true

        // Verify that couponRepository.decrementCouponCount was called once with couponId
        verify(exactly = 1) { couponRepository.decrementCouponCount(couponId) }
    }

    "redeemCoupon should return false if decrementCouponCount returns 0" {
        // Given: a coupon ID and the couponRepository.decrementCouponCount returns 0
        val couponId: Long = 456
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: redeemCoupon is called with the couponId
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: the result should be false
        result shouldBe false

        // Verify that couponRepository.decrementCouponCount was called once with couponId
        verify(exactly = 1) { couponRepository.decrementCouponCount(couponId) }
    }

    "create should save a new coupon to the repository" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 10)
        val expectedCoupon = Coupon(name = "Test Coupon", count = 10)

        every { couponRepository.save(any<Coupon>()) } returns expectedCoupon

        // When: create is called with the CouponCreate object
        couponService.create(couponCreate)

        // Then: the coupon repository should have been called with the correct coupon object
        verify {
            couponRepository.save(match {
                it.name == "Test Coupon" && it.count == 10
            })
        }
    }
})