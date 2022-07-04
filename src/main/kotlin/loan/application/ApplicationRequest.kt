package loan.application

import java.math.BigDecimal
import java.time.LocalDateTime

data class ApplicationRequest(
    val amount: BigDecimal,
    val fromDate: LocalDateTime,
    val termInDays: Long,
    val person: PersonDetailsDto
)

data class PersonDetailsDto(val firstname: String, val surname: String)