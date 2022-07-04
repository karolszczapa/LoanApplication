package loan.application

import java.math.BigDecimal
import java.time.LocalDateTime

data class FetchResponse(val id: Long, val amount: BigDecimal, val dueDate: LocalDateTime)
