@file:Suppress("NAME_SHADOWING")

import com.epam.drill.logger.*
import kotlin.native.concurrent.*
import kotlin.test.*


class TestLogger {
    @Test
    fun testMultiThreadLogging() {
        val name = "xx"
        Logging.logger(name).apply {
            logConfig.value = LoggerConfig(level = LogLevel.TRACE).freeze()
            warn { name }
            warn { name }
            warn { name }
            logConfig.value = LoggerConfig()
                .freeze()
        }
        Worker.start(true).execute(TransferMode.SAFE, { name }) { name ->
            Logging.logger(name).apply {
                warn { name }
                error { name }
                logConfig.value = LoggerConfig(level = LogLevel.TRACE).freeze()
                warn { name }
            }
        }.result
    }
}
