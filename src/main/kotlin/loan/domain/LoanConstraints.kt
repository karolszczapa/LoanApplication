package loan.domain

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.math.BigDecimal
import java.time.LocalTime

@ConstructorBinding
@ConfigurationProperties(prefix = "loan.constraints")
data class LoanConstraints(
    val minAmount: BigDecimal,
    val maxAmount: BigDecimal,
    val maxAmountRejectionMinTime: LocalTime,
    val maxAmountRejectionMaxTime: LocalTime,
    val minTermInDays: Long,
    val maxTermInDays: Long,
)
