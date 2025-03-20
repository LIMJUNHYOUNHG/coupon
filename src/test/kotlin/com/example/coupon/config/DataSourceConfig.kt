class DataSourceConfigTest : StringSpec() {

    "dataSource should return a HikariDataSource" {
        // Given: A mocked HikariConfig
        val hikariConfig = mockk<HikariConfig>()
        val dataSourceConfig = com.example.coupon.config.DataSourceConfig()

        // When: dataSource is called with the HikariConfig
        val dataSource = dataSourceConfig.dataSource(hikariConfig)

        // Then: The result should be an instance of HikariDataSource
        dataSource::class.java shouldBe HikariDataSource::class.java
    }
}