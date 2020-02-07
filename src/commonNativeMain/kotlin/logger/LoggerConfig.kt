package logger

class LoggerConfig(val isTraceEnabled: Boolean = false,
                        val isDebugEnabled: Boolean = false,
                        val isInfoEnabled: Boolean = false,
                        val isWarnEnabled: Boolean = false)