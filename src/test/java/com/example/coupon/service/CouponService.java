"redeemCoupon should return true when decrementCouponCount returns a value greater than 0" {
        // Given:
        val couponId: Long = 1L
        val couponRepository = mockk<CouponRepository>()
        val couponService = CouponService(couponRepository)

        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When:
        val result = couponService.redeemCoupon(couponId)

        // Then:
        result shouldBe true
    }

    "redeemCoupon should return false when decrementCouponCount returns 0" {
        // Given:
        val couponId: Long = 2L
        val couponRepository = mockk<CouponRepository>()
        val couponService = CouponService(couponRepository)

        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When:
        val result = couponService.redeemCoupon(couponId)

        // Then:
        result shouldBe false
    }

    "create should save a coupon to the repository" {
        // Given:
        val couponCreate = CouponCreate("TestCoupon", 50, 200L)
        val couponRepository = mockk<CouponRepository>(relaxed = true)
        val couponService = CouponService(couponRepository)

        // When:
        couponService.create(couponCreate)

        // Then:
        verify { couponRepository.save(any<Coupon>()) }
    }
})