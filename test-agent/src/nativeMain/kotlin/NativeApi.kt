import com.epam.drill.jvmapi.*
import com.epam.drill.jvmapi.gen.*
import com.epam.drill.logger.*
import kotlinx.cinterop.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set
import kotlin.native.concurrent.*


@Suppress("UNUSED", "UNUSED_PARAMETER")
@CName("Java_com_epam_drill_logger_NativeApi_nativeOutput")
fun nativeOutput(env: JNIEnv, thiz: jobject, message: jstring): Unit = withJSting {
    Logging.output(message.toKString())
}

@Suppress("UNUSED", "UNUSED_PARAMETER")
@CName("Java_com_epam_drill_logger_NativeApi_createFd")
fun nativeOutput(env: JNIEnv, thiz: jobject, message: jstring?): Unit = withJSting {
    if (message != null)
        Logging.createFd(message.toKString())
}

@Suppress("UNUSED", "UNUSED_PARAMETER")
@CName("Java_com_epam_drill_logger_NativeApi_setConfig")
fun setConfig(env: JNIEnv, thiz: jobject, rawConfig: jint) {
    val level = LogLevel.byCode(rawConfig) ?: LogLevel.ERROR
    logConfig.value = LoggerConfig(level).freeze()
}

@Suppress("UNUSED", "UNUSED_PARAMETER")
@CName("Java_com_epam_drill_logger_NativeApi_getConfig")
fun get(env: JNIEnv, thiz: jobject): jint {
    return logConfig.value.level.code
}

private inline fun withJSting(block: JStingConverter.() -> Unit) {
    val jStingConverter = JStingConverter()
    block(jStingConverter)
    jStingConverter.localStrings.forEach { (x, y) ->
        jni.ReleaseStringUTFChars!!(env, x, y)
    }
}

private class JStingConverter {
    val localStrings = mutableMapOf<jstring, CPointer<ByteVar>?>()
    fun jstring.toKString(): String {
        val nativeString = jni.GetStringUTFChars!!(env, this, null)
        localStrings[this] = nativeString
        return nativeString?.toKString()!!
    }
}
