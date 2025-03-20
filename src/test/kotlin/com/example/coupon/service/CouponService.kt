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