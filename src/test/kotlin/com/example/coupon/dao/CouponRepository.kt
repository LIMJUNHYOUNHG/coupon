import com.example.coupon.dao.Coupon
import com.example.coupon.dao.CouponRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import jakarta.persistence.EntityManager
import jakarta.persistence.LockModeType

@DataJpaTest
class CouponRepositoryTest : FunSpec() {

    @Autowired
    lateinit var couponRepository: CouponRepository

    @Autowired
    lateinit var entityManager: EntityManager

    @Transactional
    fun createCoupon(name: String, count: Long): Coupon {
        val coupon = Coupon.builder().name(name).count(count).build()
        return couponRepository.save(coupon)
    }

    init {
        test("decrementCouponCount should decrement the count if available") {
            // Given:
            val initialCount: Long = 10
            val coupon = createCoupon("Test Coupon", initialCount)
            val couponId = coupon.id!! // Assuming ID is generated on save

            entityManager.flush()
            entityManager.clear()

            // When:
            val updatedCount = couponRepository.decrementCouponCount(couponId)

            entityManager.flush()
            entityManager.clear()

            // Then:
            updatedCount shouldBe 1
            val updatedCoupon = couponRepository.findById(couponId).orElseThrow()
            updatedCoupon.getCount() shouldBe initialCount - 1
        }

        test("decrementCouponCount should not decrement if count is already zero") {
            // Given:
            val initialCount: Long = 0
            val coupon = createCoupon("Test Coupon", initialCount)
            val couponId = coupon.id!!

            entityManager.flush()
            entityManager.clear()

            // When:
            val updatedCount = couponRepository.decrementCouponCount(couponId)

            entityManager.flush()
            entityManager.clear()

            // Then:
            updatedCount shouldBe 0
            val updatedCoupon = couponRepository.findById(couponId).orElseThrow()
            updatedCoupon.getCount() shouldBe initialCount
        }
    }
}