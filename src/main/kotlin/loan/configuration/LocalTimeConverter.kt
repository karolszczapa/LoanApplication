package loan.configuration

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Component
@ConfigurationPropertiesBinding
class LocalTimeConverter : Converter<String, LocalTime> {
    override fun convert(source: String) = LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm:ss"))
}