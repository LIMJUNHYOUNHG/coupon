class DataSourceConfigTest : StringSpec({
    "dataSource should return a HikariDataSource" {
        // Given: A HikariConfig object.  We mock it here, but in reality, it would be a validly configured object.
        val hikariConfig = mockk<com.zaxxer.hikari.HikariConfig>()
        val dataSourceConfig = com.example.coupon.config.DataSourceConfig()

        // When: The dataSource function is called.  Since the previous implementation was removed and a new one added, we will check to make sure this compiles.  Without the details of the new implementation, we can't test its behavior.
        val dataSource = dataSourceConfig.dataSource(hikariConfig) // Check compilation

        // Then: We can't properly test the behavior without implementation details.
        dataSource::class.java.name shouldBe "com.zaxxer.hikari.HikariDataSource"
    }
})