package loan.application

import loan.domain.LoanConstraints
import loan.domain.Loan
import loan.domain.PersonDetails
import loan.repository.LoanRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

@Service
class LoanService(
    @Value("\${loan.interest}") private val interest: BigDecimal,
    private val loanConstraints: LoanConstraints,
    private val loanRepository: LoanRepository
) {
    companion object {
        private fun map(dto: PersonDetailsDto): PersonDetails = PersonDetails(dto.firstname, dto.surname)
    }

    @Transactional
    fun apply(request: ApplicationRequest): ApplicationReponse {
        val loan = Loan(request.amount, request.fromDate, request.termInDays, interest, map(request.person))
        loan.checkIfIsApplicable(loanConstraints)
        val savedLoan = loanRepository.save(loan)
        return ApplicationReponse(savedLoan.id!!)
    }

    @Transactional(readOnly = true)
    fun fetch(id: Long): FetchResponse {
        val loan = findOrThrow(id)
        return FetchResponse(loan.id!!, loan.amount, loan.dueDate)
    }

    @Transactional
    fun extend(request: ExtensionRequest) {
        val loan = findOrThrow(request.id)
        loan.extend(request.termInDaysPlus, loanConstraints)
        loanRepository.save(loan)
    }

    private fun findOrThrow(id: Long) = loanRepository.findById(id)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "loan with id ${id} not found") }

}