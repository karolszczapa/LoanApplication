package loan.domain

import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class LoanTest {
    companion object {
        private val MIN_AMOUNT = BigDecimal.valueOf(100)
        private val MAX_AMOUNT = BigDecimal.valueOf(5000)
        private val MAX_AMOUNT_REJECTION_MIN_TIME = LocalTime.of(0, 0)
        private val MAX_AMOUNT_REJECTION_MAX_TIME = LocalTime.of(6, 0)
        private val ANY_DATE = LocalDate.of(2022, 1, 1)
        private val GOOD_TIME = LocalTime.of(12, 0, 0)
        private const val MIN_DAYS: Long = 15
        private const val MAX_DAYS: Long = 180
        private val ANY_GOOD_DATE_TIME: LocalDateTime = LocalDateTime.of(ANY_DATE, GOOD_TIME)
        private const val OK_TERM_IN_DAYS: Long = 90

        val constraints = LoanConstraints(
            MIN_AMOUNT,
            MAX_AMOUNT,
            MAX_AMOUNT_REJECTION_MIN_TIME,
            MAX_AMOUNT_REJECTION_MAX_TIME,
            MIN_DAYS,
            MAX_DAYS
        )

        fun defaultLoan(
            amount: BigDecimal = BigDecimal.valueOf(1000),
            fromDate: LocalDateTime = ANY_GOOD_DATE_TIME,
            termInDays: Long = OK_TERM_IN_DAYS,
            principal: BigDecimal = BigDecimal.valueOf(0.1),
            personDetails: PersonDetails = PersonDetails("Charlie", "Splinter")
        ) = Loan(amount, fromDate, termInDays, principal, personDetails)
    }

    @Test
    fun validLoan() {
        assertDoesNotThrow { defaultLoan().checkIfIsApplicable(constraints) }
    }

    @Test
    fun rejectionWhenAmountAboveLoanLimit() {
        assertThrows<LoanException> {
            defaultLoan(amount = MAX_AMOUNT.plus(BigDecimal.ONE)).checkIfIsApplicable(
                constraints
            )
        }
    }

    @Test
    fun rejectionWhenAmountBelowLoanLimit() {
        assertThrows<LoanException> {
            defaultLoan(amount = MIN_AMOUNT.minus(BigDecimal.ONE)).checkIfIsApplicable(
                constraints
            )
        }
    }

    @Test
    fun rejectionWhenMaxAmountBeforeMaxTime() {
        assertThrows<LoanException> {
            defaultLoan(
                amount = MAX_AMOUNT,
                LocalDateTime.of(ANY_DATE, MAX_AMOUNT_REJECTION_MAX_TIME.minusSeconds(1))
            ).checkIfIsApplicable(constraints)
        }
    }

    @Test
    fun noRejectionWhenMaxAmountIsAtMaxTime() {
        assertDoesNotThrow {
            defaultLoan(
                amount = MAX_AMOUNT,
                fromDate = LocalDateTime.of(ANY_DATE, MAX_AMOUNT_REJECTION_MAX_TIME)
            ).checkIfIsApplicable(constraints)
        }
    }

    @Test
    fun rejectionWhenTermIsBelowMinDays() {
        assertThrows<LoanException> { defaultLoan(termInDays = MIN_DAYS - 1).checkIfIsApplicable(constraints) }
    }

    @Test
    fun rejectionWhenTermIsAboveMaxDays() {
        assertThrows<LoanException> { defaultLoan(termInDays = MAX_DAYS + 1).checkIfIsApplicable(constraints) }
    }

    @Test
    fun checkDueDate() {
        val loan = defaultLoan(fromDate = ANY_GOOD_DATE_TIME, termInDays = OK_TERM_IN_DAYS)
        assertEquals(loan.dueDate, ANY_GOOD_DATE_TIME.plusDays(OK_TERM_IN_DAYS))
    }

    @Test
    fun exctendOk() {
        assertDoesNotThrow {
            defaultLoan(
                fromDate = ANY_GOOD_DATE_TIME, termInDays = OK_TERM_IN_DAYS
            ).extend(1, constraints)
        }
    }

    @Test
    fun tryToExtendAboveMaxTerm() {
        assertThrows<LoanException> { defaultLoan(termInDays = MAX_DAYS).extend(1, constraints) }
    }
}