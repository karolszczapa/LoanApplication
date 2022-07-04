package loan.domain

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "loan")
class Loan(
    val amount: BigDecimal, //better is to use MonetaryAmount
    private val fromDate: LocalDateTime,
    private var termInDays: Long,
    principalInterest: BigDecimal,
    @Embedded val personDetails: PersonDetails,
    @Id @GeneratedValue var id: Long? = null
) {
    private val amountToPay: BigDecimal = amount.multiply(principalInterest) //better is to use MonetaryAmount
    val dueDate: LocalDateTime get() = fromDate.plusDays(termInDays)

    fun extend(termInDaysPlus: Long, loanConstrains: LoanConstraints){
        termInDays += termInDaysPlus
        checkMaxTerm(loanConstrains)
    }

    fun checkIfIsApplicable(constrains: LoanConstraints) {
        checkAmount(constrains)
        checkTerm(constrains)
    }

    private fun checkAmount(constrains: LoanConstraints) {
        checkMinAmount(constrains)
        checkMaxAmount(constrains)
        checkMaxAmountAndTime(constrains)
    }

    private fun checkTerm(constraints: LoanConstraints) {
        checkMinTerm(constraints)
        checkMaxTerm(constraints)
    }

    private fun checkMaxAmountAndTime(constraints: LoanConstraints) {
        val time = fromDate.toLocalTime()
        if (amount == constraints.maxAmount && time >= constraints.maxAmountRejectionMinTime && time < constraints.maxAmountRejectionMaxTime) {
            throw LoanException(
                "loan is rejected because the max amount ${constraints.maxAmount} was applied at $time what is between " +
                        "${constraints.maxAmountRejectionMinTime} and ${constraints.maxAmountRejectionMaxTime}"
            )
        }
    }

    private fun checkMaxAmount(constraints: LoanConstraints) {
        if (amount > constraints.maxAmount) {
            throw LoanException("loan is rejected because amount $amount is too high, max amount is ${constraints.maxAmount}")
        }
    }

    private fun checkMinAmount(constraints: LoanConstraints) {
        if (amount < constraints.minAmount) {
            throw LoanException("loan is rejected because amount $amount is too low, min amount is ${constraints.minAmount}")
        }
    }

    private fun checkMaxTerm(constraints: LoanConstraints) {
        if (termInDays > constraints.maxTermInDays) {
            throw LoanException("amount $amount is too high, max amount is ${constraints.maxTermInDays} days")
        }
    }

    private fun checkMinTerm(constraints: LoanConstraints) {
        if (termInDays < constraints.minTermInDays) {
            throw LoanException("term $termInDays is too low, min term is ${constraints.minTermInDays} days")
        }
    }
}
