package loan.domain

class LoanException(message: String?, cause: Throwable? = null) : RuntimeException(message, cause)