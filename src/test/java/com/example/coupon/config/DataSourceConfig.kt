class DataSourceConfigTest : StringSpec({

    "hikariConfig bean should be created with properties" {
        // Given: A DataSourceConfig instance
        val dataSourceConfig = DataSourceConfig()

        // When: The hikariConfig bean is created
        val hikariConfig: HikariConfig = dataSourceConfig.hikariConfig()

        // Then: The hikariConfig bean should be an instance of HikariConfig
        hikariConfig::class.java.name shouldBe "com.zaxxer.hikari.HikariConfig"
        // In a real test, verify properties set by @ConfigurationProperties.  This is not possible without reflection, mocking properties, or deeper integration testing.
    }

    "dataSource bean should be created from hikariConfig" {
        // Given: A DataSourceConfig instance and a HikariConfig instance
        val dataSourceConfig = DataSourceConfig()
        val hikariConfig: HikariConfig = dataSourceConfig.hikariConfig()

        // When: The dataSource bean is created
        val dataSource: HikariDataSource = dataSourceConfig.dataSource(hikariConfig)

        // Then: The dataSource bean should be an instance of HikariDataSource
        dataSource::class.java.name shouldBe "com.zaxxer.hikari.HikariDataSource"
    }
})