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