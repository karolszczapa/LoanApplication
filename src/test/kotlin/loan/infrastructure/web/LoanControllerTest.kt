package loan.infrastructure.web

import loan.application.ApplicationReponse
import loan.application.ApplicationRequest
import loan.application.ExtensionRequest
import loan.application.FetchResponse
import loan.application.PersonDetailsDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(SpringExtension::class)
@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class LoanControllerTest(
    @Autowired private val webTestClient: WebTestClient,

    ) {
    companion object {
        val applicationRequest = ApplicationRequest(
            BigDecimal.valueOf(1000), LocalDateTime.of(2022, 1, 1, 12, 0, 0),
            90, PersonDetailsDto("Charlie", "Splinter")
        )
    }

    @Test
    fun loanApplicationHappyPathTest() {
        val body = applyForLoan(applicationRequest)

        assertNotEquals(0, body.id)
    }

    @Test
    fun fetchHappyPathTest() {
        val id = applyForLoan(applicationRequest).id

        val response = fetchLoan(id)

        assertEquals(id, response.id)
        assertEquals(applicationRequest.amount.compareTo(response.amount), 0)
        assertEquals(applicationRequest.fromDate.plusDays(applicationRequest.termInDays), response.dueDate)
    }

    @Test
    fun fetchNotFound() {
        val id = applyForLoan(applicationRequest).id

        val response = fetchLoan(id)

        assertEquals(id, response.id)
        assertEquals(applicationRequest.amount.compareTo(response.amount), 0)
        assertEquals(applicationRequest.fromDate.plusDays(applicationRequest.termInDays), response.dueDate)
    }

    @Test
    fun extensionHappyPathTest() {
        val id = applyForLoan(applicationRequest).id

        extendLoan(ExtensionRequest(id, 30))

        val response = fetchLoan(id)
        assertEquals(applicationRequest.fromDate.plusDays(applicationRequest.termInDays).plusDays(30), response.dueDate)
    }

    private fun fetchLoan(id: Long) = webTestClient
        .get()
        .uri("/loan/fetch/${id}")
        .exchange()
        .expectStatus().is2xxSuccessful
        .expectBody<FetchResponse>()
        .returnResult()
        .responseBody!!

    private fun applyForLoan(applicationRequest: ApplicationRequest) = webTestClient
        .post()
        .uri("/loan/apply")
        .bodyValue(applicationRequest)
        .exchange()
        .expectStatus().is2xxSuccessful
        .expectBody<ApplicationReponse>()
        .returnResult()
        .responseBody!!

    private fun extendLoan(extensionRequest: ExtensionRequest) {
        webTestClient
            .post()
            .uri("/loan/extend")
            .bodyValue(extensionRequest)
            .exchange()
            .expectStatus().is2xxSuccessful
    }
}