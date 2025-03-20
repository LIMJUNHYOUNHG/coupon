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