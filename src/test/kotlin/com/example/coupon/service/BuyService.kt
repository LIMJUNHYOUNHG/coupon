describe("BuyService") {
        val buyService = BuyService()

        it("should increment the buy count for a given product") {
            // Given: A product ID that hasn't been bought yet
            val productId = 123L

            // When: the buy method is called with the product ID
            assertDoesNotThrow { buyService.buy(productId) }

            // Then: The product's count in the buyList should be 1
            // Note: accessing internal state directly for testing purposes only
            val internalBuyList = javaClass.getDeclaredField("buyList").apply { isAccessible = true }.get(buyService) as MutableMap<Long, Long>
            internalBuyList[productId] shouldBe 1L
        }

        it("should increment the buy count correctly for existing products") {
            // Given: A product ID that already has a buy count
            val productId = 456L
            val internalBuyList = javaClass.getDeclaredField("buyList").apply { isAccessible = true }.get(buyService) as MutableMap<Long, Long>
            internalBuyList[productId] = 5L

            // When: the buy method is called again with the same product ID
            buyService.buy(productId)

            // Then: the product's count in the buyList should increment to 6.
            internalBuyList[productId] shouldBe 6L
        }
    }
})