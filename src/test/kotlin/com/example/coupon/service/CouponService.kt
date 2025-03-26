"redeemCoupon should return true when decrementCouponCount returns a positive value" {
        // Given:
        val couponId: Long = 1L
        val couponRepository: CouponRepository = mockk()
        val couponService = CouponService(couponRepository)

        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When:
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then:
        result shouldBe true

        verify(exactly = 1) { couponRepository.decrementCouponCount(couponId) }
    }

    "redeemCoupon should return false when decrementCouponCount returns zero" {
        // Given:
        val couponId: Long = 1L
        val couponRepository: CouponRepository = mockk()
        val couponService = CouponService(couponRepository)

        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When:
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then:
        result shouldBe false

        verify(exactly = 1) { couponRepository.decrementCouponCount(couponId) }
    }

     "create should save a coupon to the repository" {
        // Given:
        val couponCreate = CouponCreate("Test Coupon", 100, 5L)
        val couponRepository: CouponRepository = mockk()
        val couponService = CouponService(couponRepository)

        val expectedCoupon = Coupon.builder()
            .name(couponCreate.name())
            .count(couponCreate.count())
            .build()

        every { couponRepository.save(any<Coupon>()) } returnsArgument 0 // return the argument itself

        // When:
        couponService.create(couponCreate)

        // Then:
        verify(exactly = 1) { couponRepository.save(match {
            it.name == expectedCoupon.name && it.count == expectedCoupon.count
        }) }
    }
})