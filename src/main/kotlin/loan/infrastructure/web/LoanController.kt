package loan.infrastructure.web

import loan.application.ApplicationReponse
import loan.application.ApplicationRequest
import loan.application.ExtensionRequest
import loan.application.FetchResponse
import loan.application.LoanService
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/loan")
class LoanController(
    private val loanService: LoanService
) {
    companion object{
        private val log = KotlinLogging.logger {}
    }

    @PostMapping(
        value = ["/apply"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun apply(@RequestBody request: ApplicationRequest): ApplicationReponse {
        log.info(">> Loan application request: $request")
        val response = loanService.apply(request)
        log.info("<< Loan application response $response")
        return response
    }

    @GetMapping(
        value = ["/fetch/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun fetch(@PathVariable id: Long): FetchResponse {
        log.info(">> Loan fetch request: $id")
        val response = loanService.fetch(id)
        log.info("<< Loan fetch response $response")
        return response
    }

    @PostMapping(
        value = ["/extend"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun fetch(@RequestBody request: ExtensionRequest) {
        log.info(">> Loan extend request: $request")
        loanService.extend(request)
    }
}