class DataSourceConfigTest : StringSpec({
    "Context loads successfully" {
        // Given: An ApplicationContextRunner
        val contextRunner = ApplicationContextRunner()
            .withUserConfiguration(DataSourceConfig::class.java)

        // When: Running the context
        contextRunner.run { context ->
            // Then: The context should be successfully loaded.
            context.getBean(DataSourceConfig::class.java) shouldBe context.getBean(DataSourceConfig::class.java)
        }
    }
})