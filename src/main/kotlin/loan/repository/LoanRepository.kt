package loan.repository

import loan.domain.Loan
import org.springframework.data.repository.CrudRepository

interface LoanRepository: CrudRepository<Loan, Long> {
}