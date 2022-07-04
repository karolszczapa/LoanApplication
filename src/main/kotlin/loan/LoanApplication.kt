package loan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class LoanApplication

fun main(args: Array<String>) {
  runApplication<LoanApplication>(*args)
}