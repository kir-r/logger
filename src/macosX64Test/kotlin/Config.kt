import com.epam.drill.logger.LoggerConfig
import com.epam.drill.logger.logConfig
import mu.KotlinLogging
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze
import kotlin.test.Test


class TestLogger {

    @Test
    fun xa() {
        val logger = KotlinLogging.logger("xx")
        logger.trace { "xx" }
        logger.info { "xx" }
        logger.warn { "xx" }
        logConfig.value = logConfig.value.copy(true, true, true, true).freeze()
        logger.trace { "xx" }
        logger.info { "xx" }
        logger.warn { "xx" }
        logConfig.value = LoggerConfig().freeze()
        Worker.start(true).execute(TransferMode.SAFE, {}) {
            val logger = KotlinLogging.logger("xx")
            logger.trace { "xx" }
            logger.info { "xx" }
            logger.warn { "xx" }
            logConfig.value = logConfig.value.copy(true, true, true, true).freeze()
            logger.trace { "xx" }
            logger.info { "xx" }
            logger.warn { "xx" }
        }.result

    }


}