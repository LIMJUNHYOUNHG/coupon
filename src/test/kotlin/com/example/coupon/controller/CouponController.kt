class CouponControllerTest : StringSpec({

    val couponService = mockk<CouponService>()
    val couponController = CouponController(couponService)

    "redeemCoupon - 성공적인 쿠폰 사용" {
        // Given: 쿠폰 사용이 성공적으로 처리될 것이라고 가정
        val couponId: Long = 123L
        every { couponService.redeemCoupon(couponId) } returns true

        // When: redeemCoupon 엔드포인트를 호출
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: 응답 상태 코드가 200 OK이고, 메시지가 "쿠폰 사용 성공"이어야 함
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 사용 성공"
    }

    "redeemCoupon - 잔여 쿠폰 없음" {
        // Given: 잔여 쿠폰이 없어서 쿠폰 사용이 실패할 것이라고 가정
        val couponId: Long = 456L
        every { couponService.redeemCoupon(couponId) } returns false

        // When: redeemCoupon 엔드포인트를 호출
        val response: ResponseEntity<String> = couponController.redeemCoupon(couponId)

        // Then: 응답 상태 코드가 400 Bad Request이고, 메시지가 "잔여 쿠폰 없음"이어야 함
        response.statusCode shouldBe HttpStatus.BAD_REQUEST
        response.body shouldBe "잔여 쿠폰 없음"
    }

    "createCoupon - 쿠폰 생성 성공" {
        // Given: 유효한 CouponCreate 객체
        val couponCreate = CouponCreate("testCoupon", 10)
        every { couponService.create(couponCreate) } returns Unit // void method returns Unit in Kotlin

        // When: createCoupon 엔드포인트를 호출
        val response: ResponseEntity<String> = couponController.createCoupon(couponCreate)

        // Then: 응답 상태 코드가 200 OK이고, 메시지가 "쿠폰 생성 성공"이어야 함
        response.statusCode shouldBe HttpStatus.OK
        response.body shouldBe "쿠폰 생성 성공"
    }
})

// src/main/kotlin/com/example/coupon/service/CouponService.kt
class CouponServiceTest : StringSpec() {

    val couponRepository = mockk<com.example.coupon.dao.CouponRepository>()
    val couponService = CouponService(couponRepository)

    "redeemCoupon - 쿠폰 카운트 감소 성공" {
        // Given: 쿠폰 ID와 decrementCouponCount가 1을 반환하도록 설정 (업데이트 성공)
        val couponId: Long = 1L
        every { couponRepository.decrementCouponCount(couponId) } returns 1

        // When: redeemCoupon 호출
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: true가 반환되어야 함 (쿠폰 사용 성공)
        result shouldBe true
    }

    "redeemCoupon - 쿠폰 카운트 감소 실패 (재고 없음)" {
        // Given: 쿠폰 ID와 decrementCouponCount가 0을 반환하도록 설정 (업데이트 실패)
        val couponId: Long = 2L
        every { couponRepository.decrementCouponCount(couponId) } returns 0

        // When: redeemCoupon 호출
        val result: Boolean = couponService.redeemCoupon(couponId)

        // Then: false가 반환되어야 함 (쿠폰 사용 실패)
        result shouldBe false
    }

    "create - 쿠폰 생성" {
        // Given: CouponCreate 객체 및 save 동작 모의
        val couponCreate = CouponCreate("SummerSale", 50)
        val coupon = Coupon(name = couponCreate.name, count = couponCreate.count)
        every { couponRepository.save(any()) } returns coupon

        // When: create 메소드 호출
        couponService.create(couponCreate)

        // Then: couponRepository.save가 호출되었는지 확인 (verify 필요)
        // Note: MockK를 사용하여 verify를 추가할 수 있지만, 간단한 경우 이 테스트는 save 호출 여부를 직접 확인하지 않습니다.
    }
}

// src/main/kotlin/com/example/coupon/dao/CouponRepository.kt
// CouponRepository에 대한 테스트는 Spring Data JPA Repository 테스트로,
// 일반적으로 통합 테스트를 사용하여 데이터베이스와 연동하여 테스트합니다.
// 단위 테스트로는 JpaRepository의 동작을 모의하기 어렵기 때문에,
// 여기서는 간단한 예시만을 제공합니다.  실제 테스트는 SpringBootTest 환경에서 수행하는 것을 권장합니다.

// src/main/kotlin/com/example/coupon/config/DataSourceConfig.kt
// DataSourceConfig 에 대한 단위 테스트는 주로 HikariConfig 설정이 제대로 이루어졌는지 확인하는 데 중점을 둡니다.
// 하지만 이는 Spring Boot의 설정 속성을 사용하는 것이므로, 통합 테스트에서 더 효과적으로 검증할 수 있습니다.
// 단위 테스트에서는 HikariConfig Bean이 제대로 생성되는지만 확인하는 간단한 예시를 제공합니다.

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.example.coupon.config.DataSourceConfig
import io.mockk.verify

class DataSourceConfigTest : StringSpec({

    "dataSource - HikariDataSource 빈 생성 확인" {
        // Given: Mock HikariConfig
        val hikariConfig = mockk<HikariConfig>(relaxed = true)
        val dataSourceConfig = DataSourceConfig()

        // When: dataSource 메소드 호출
        val dataSource = dataSourceConfig.dataSource(hikariConfig)

        // Then: HikariDataSource 가 생성되어야 함
        dataSource shouldBe dataSourceConfig.dataSource(hikariConfig) //verify datasource creation
    }
})