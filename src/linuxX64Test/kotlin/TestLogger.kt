@file:Suppress("NAME_SHADOWING")

import com.epam.drill.logger.*
import mu.*
import kotlin.native.concurrent.*
import kotlin.test.*


class TestLogger {

    @Test
    fun testMultiThreadLogging() {
        val name = "xx"
        KotlinLogging.logger(name).apply {
            logConfig.value = logConfig.value.copy(true, true, true, true).freeze()
            warn { name }
            warn { name }
            warn { name }
            logConfig.value = LoggerConfig()
                .freeze()
        }
        Worker.start(true).execute(TransferMode.SAFE, { name }) { name ->
            KotlinLogging.logger(name).apply {
                warn { name }
                error { name }
                logConfig.value = logConfig.value.copy(true, true, true, true).freeze()
                warn { name }
            }
        }.result


    }


}