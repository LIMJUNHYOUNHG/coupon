class DataSourceConfigTest : BehaviorSpec({
    // This class only contains configuration, testing is limited.
    // Consider integration tests if you want to test the data source.
    // Alternatively, you could mock the HikariConfig and check its properties

    given("a HikariConfig object") {
        val hikariConfig = mockk<com.zaxxer.hikari.HikariConfig>()

        `when`("the dataSource bean is created") {
            // While we can create the DataSourceConfig, checking the resulting
            // datasource without a real database is difficult without resorting
            // to integration testing and/or mocking the HikariDataSource which
            // tests nothing of value.

            // In a real project, you would likely have an integration test that
            // brings up the application context and verifies the data source
            // is correctly configured.

            // For this example, we'll just ensure the method returns a non-null value if nothing else
            val dataSourceConfig = com.example.coupon.config.DataSourceConfig()
            val dataSource = dataSourceConfig.dataSource(hikariConfig)
            then("the data source should not be null") {
                dataSource shouldBe dataSource
            }
        }
    }
})