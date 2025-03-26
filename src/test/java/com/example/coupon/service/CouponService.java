val couponRepository = mockk<com.example.coupon.dao.CouponRepository>()
    val couponService = com.example.coupon.service.CouponService(couponRepository)

    "redeemCoupon should return true when decrementCouponCount returns a positive value" {
        // Given: A coupon ID and the repository returns 1 (meaning a coupon was decremented)
        val couponId: Long = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: redeemCoupon is called
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be true
        result shouldBe true
    }

    "redeemCoupon should return false when decrementCouponCount returns 0" {
        // Given: A coupon ID and the repository returns 0 (meaning no coupon was decremented)
        val couponId: Long = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: redeemCoupon is called
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: The result should be false
        result shouldBe false
    }

    "create should save a coupon using the repository" {
        // Given: A CouponCreate object
        val couponCreate = com.example.coupon.controller.CouponCreate("Test Coupon", 10, 5L)
        val expectedCoupon = com.example.coupon.dao.Coupon.builder().name("Test Coupon").count(5L).build()
        every { couponRepository.save(any()) } returns mockk() // We don't care about the return of save.

        // When: create is called
        couponService.create(couponCreate)

        // Then: repository.save should be called
        io.mockk.verify { couponRepository.save(match { it.name == "Test Coupon" && it.count == 5L }) }

    }
})