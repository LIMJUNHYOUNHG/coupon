import com.example.coupon.dao.Coupon
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.EntityManager

@DataJpaTest
@TestPropertySource(properties = ["spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"])
class CouponRepositoryTest : StringSpec() {

    @Autowired
    lateinit var couponRepository: CouponRepository

    @Autowired
    lateinit var entityManager: EntityManager

    init {
        "decrementCouponCount should decrement the count when count is greater than 0" {
            // Given: a coupon with an initial count of 5
            val coupon = Coupon(name = "Test Coupon", price = 100, count = 5)
            couponRepository.save(coupon)
            entityManager.flush()
            entityManager.clear()

            // When: decrementCouponCount is called with the coupon's ID
            val updatedCount = couponRepository.decrementCouponCount(coupon.id!!)

            // Then: the updatedCount should be 1, and the coupon's count in the database should be 4
            updatedCount shouldBe 1

            val retrievedCoupon = couponRepository.findById(coupon.id!!).orElse(null)
            retrievedCoupon?.count shouldBe 4
        }

        "decrementCouponCount should not decrement the count when count is 0" {
            // Given: a coupon with an initial count of 0
            val coupon = Coupon(name = "Test Coupon", price = 100, count = 0)
            couponRepository.save(coupon)
            entityManager.flush()
            entityManager.clear()

            // When: decrementCouponCount is called with the coupon's ID
            val updatedCount = couponRepository.decrementCouponCount(coupon.id!!)

            // Then: the updatedCount should be 0, and the coupon's count in the database should remain 0
            updatedCount shouldBe 0

            val retrievedCoupon = couponRepository.findById(coupon.id!!).orElse(null)
            retrievedCoupon?.count shouldBe 0
        }

        "decrementCouponCount should return 0 when coupon does not exist" {
            // Given: A non-existent coupon id
            val nonExistentCouponId:Long = 9999

            // When: decrementCouponCount is called with a non-existent coupon id
            val updatedCount = couponRepository.decrementCouponCount(nonExistentCouponId)

            // Then: the updatedCount should be 0
            updatedCount shouldBe 0
        }
    }
}