class DataSourceConfigTest : StringSpec({

    "dataSource should return a HikariDataSource instance" {
        // Given: A HikariConfig object.
        val hikariConfig = HikariConfig()
        val dataSourceConfig = DataSourceConfig()

        // When: dataSource function is called with the HikariConfig.
        val dataSource = dataSourceConfig.dataSource(hikariConfig)

        // Then: It should return a HikariDataSource instance.
        dataSource::class shouldBe HikariDataSource::class
    }

    "dataSource new definition test" {
        // Given: A HikariConfig object.
        val hikariConfig = HikariConfig()
        val dataSourceConfig = DataSourceConfig()

        // When: dataSource function is called with the HikariConfig.
        val dataSource = dataSourceConfig.dataSource(hikariConfig)

        // Then: It should return a HikariDataSource instance.
        dataSource::class shouldBe HikariDataSource::class
    }
})

// src/main/kotlin/com/example/coupon/controller/CouponController.kt
class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon should return OK if coupon is redeemed successfully" {
        // Given: A couponId and the couponService returns true (success).
        val couponId: Long = 123
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon method is called.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be OK with the appropriate message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon should return BadRequest if coupon redemption fails" {
        // Given: A couponId and the couponService returns false (failure).
        val couponId: Long = 456
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon method is called.
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: The response should be BadRequest with the appropriate message.
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon should return OK if coupon is created successfully" {
        // Given: A CouponCreate object.
        val couponCreate = CouponCreate("TestCoupon", 10)
        every { couponService.create(couponCreate) } returns Unit

        // When: createCoupon method is called.
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: The response should be OK with the appropriate message.
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})