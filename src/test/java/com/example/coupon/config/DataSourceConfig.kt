class DataSourceConfigTest : StringSpec({

    "hikariConfig bean creation" {
        // Given: DataSourceConfig instance
        val dataSourceConfig = DataSourceConfig()

        // When: Creating hikariConfig bean
        val hikariConfig = dataSourceConfig.hikariConfig()

        // Then: Assert that the returned object is a HikariConfig instance
        hikariConfig::class.java.name shouldBe "com.zaxxer.hikari.HikariConfig"
    }

    "dataSource bean creation" {
        // Given: DataSourceConfig instance and a HikariConfig
        val dataSourceConfig = DataSourceConfig()

        // Create a simple H2 in-memory datasource for the test
        val dataSource = EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .build()

        val hikariConfig = HikariConfig()
        hikariConfig.dataSource = dataSource

        // When: Creating dataSource bean
        val actualDataSource = dataSourceConfig.dataSource(hikariConfig)

        // Then: Assert that the returned object is a HikariDataSource instance
        actualDataSource::class.java.name shouldBe "com.zaxxer.hikari.HikariDataSource"
    }
})