package mu

import com.epam.drill.logger.*

object KotlinLogging {

    private val _file = Atom<String?>(null)
    val _fd = Atom<Int?>(null)

    var file: String?
        get() = _file.value
        set(value) {
            createFd(value)
            _file.value = value
        }

    /**
     * This method allow defining the logger in a file in the following way:
     * ```
     * val logger = KotlinLogging.logger {}
     * ```
     */
    fun logger(func: () -> Unit): KLogger {
        val message = func::class.qualifiedName?.replace(".${func::class.simpleName}", "")
        return DrillLogger(message ?: "unknown logger")
    }

    fun logger(name: String): KLogger {
        return DrillLogger(name)
    }
}