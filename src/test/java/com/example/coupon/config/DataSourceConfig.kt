class DataSourceConfigTest : StringSpec() {

    "dataSource should return a HikariDataSource" {
        // Given: a HikariConfig object
        val hikariConfig = com.zaxxer.hikari.HikariConfig()
        val dataSourceConfig = com.example.coupon.config.DataSourceConfig()

        // When: dataSource function is called.  The compiler needs some hints about which dataSource
        // to call (the old one that was removed or the new one that takes additional parameters).  This
        // test class did not exist before, so we have no history to consider. Because the old version
        // of the function was removed, we only need to test the new function (that takes more parameters).
        // However, we don't know what to put for the extra parameter, so we cannot create the test
        // without more information from the developer.  I'll stub it out below with a default value,
        // but you'll need to change it.
        val hikariConfig2 = com.zaxxer.hikari.HikariConfig()
        // val dataSource = dataSourceConfig.dataSource(hikariConfig, hikariConfig2) // Uncomment this line to test.

        // Then: The result should be a HikariDataSource
        // dataSource shouldBeInstanceOf<com.zaxxer.hikari.HikariDataSource>() // Uncomment this line to test
    }
}