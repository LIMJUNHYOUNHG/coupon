import com.example.coupon.config.DataSourceConfig
import com.zaxxer.hikari.HikariConfig
import io.kotest.core.spec.style.FunSpec
import io.mockk.every
import io.mockk.mockk
import io.kotest.matchers.shouldBe

class DataSourceConfigTest : FunSpec({

    test("dataSource should return a HikariDataSource") {
        // Given: a mocked HikariConfig
        val hikariConfig = mockk<HikariConfig>()
        val dataSourceConfig = DataSourceConfig()

        // When: dataSource is called with the mocked config
        val dataSource = dataSourceConfig.dataSource(hikariConfig)

        // Then: The data source should not be null, indicating successful creation
        dataSource shouldBeNotNull
    }

    test("hikariConfig should return a HikariConfig instance") {
        // Given: an instance of DataSourceConfig
        val dataSourceConfig = DataSourceConfig()

        // When: hikariConfig is called
        val config = dataSourceConfig.hikariConfig()

        // Then: The returned value should be an instance of HikariConfig
        config shouldBe a<HikariConfig>()
    }
})