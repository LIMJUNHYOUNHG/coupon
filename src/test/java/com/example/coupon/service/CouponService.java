class CouponServiceTest : StringSpec({

    val couponRepository = mockk<CouponRepository>()
    val couponService = CouponService(couponRepository)

    "redeemCoupon: Should decrement coupon count and return true if updated count is greater than 0" {
        // Given: A coupon ID and the repository returns 1 (updated successfully)
        val couponId: Long = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: redeemCoupon method is called with the coupon ID
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be true
        result shouldBe true
        verify { couponRepository.decrementCouponCount(couponId) }
    }

    "redeemCoupon: Should decrement coupon count and return false if updated count is not greater than 0" {
        // Given: A coupon ID and the repository returns 0 (no coupon updated)
        val couponId: Long = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: redeemCoupon method is called with the coupon ID
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be false
        result shouldBe false
        verify { couponRepository.decrementCouponCount(couponId) }
    }

    "create: Should save a coupon to the repository" {
        // Given: A CouponCreate object
        val couponCreate = CouponCreate("Test Coupon", 100, 10L)
        val expectedCoupon = Coupon.builder().name(couponCreate.name()).count(couponCreate.count()).build()
        every { couponRepository.save(any<Coupon>()) } returns mockk() // Return type matters.  Since save is void, mock it to return Unit implicitly.
        // When: create method is called with the CouponCreate object
        couponService.create(couponCreate)

        // Then: The coupon repository should save a coupon with the correct values
        verify { couponRepository.save(match { it.name == couponCreate.name() && it.count == couponCreate.count() }) }
    }
})