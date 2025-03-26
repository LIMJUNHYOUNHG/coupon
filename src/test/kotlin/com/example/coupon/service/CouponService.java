import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import com.example.coupon.service.CouponService
import com.example.coupon.dao.CouponRepository
import com.example.coupon.controller.CouponCreate