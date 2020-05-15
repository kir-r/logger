import com.epam.drill.logger.*
import mu.*
import kotlin.test.*

class DrillLoggerTest {

    @Test
    fun commonTest() {
        val name = "xx"
        KotlinLogging.logger(name).apply {
            logConfig.value = logConfig.value.copy(true, true, true, true).platformFreeze()
            error { name }
            warn { name }
            warn { name }
            val t = RuntimeException("wwww")
            warn(t) { name }
            logConfig.value = LoggerConfig()
                .platformFreeze()
        }
    }
}