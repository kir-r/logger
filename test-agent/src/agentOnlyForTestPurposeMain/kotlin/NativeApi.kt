import com.epam.drill.jvmapi.*
import com.epam.drill.jvmapi.gen.*
import com.epam.drill.logger.*
import kotlinx.cinterop.*
import kotlinx.serialization.protobuf.*
import mu.*
import kotlin.native.concurrent.*


@Suppress("UNUSED", "UNUSED_PARAMETER")
@CName("Java_com_epam_drill_logger_NativeApi_nativeOutput")
fun nativeOutput(env: JNIEnv, thiz: jobject, message: jstring): Unit = withJSting {
    KotlinLogging.output(message.toKString())
}

@Suppress("UNUSED", "UNUSED_PARAMETER")
@CName("Java_com_epam_drill_logger_NativeApi_createFd")
fun nativeOutput(env: JNIEnv, thiz: jobject, message: jstring?): Unit = withJSting {
    if (message != null)
        KotlinLogging.createFd(message.toKString())
}

@Suppress("UNUSED", "UNUSED_PARAMETER")
@CName("Java_com_epam_drill_logger_NativeApi_setConfig")
fun setConfig(env: JNIEnv, thiz: jobject, rawConfig: jbyteArray) {
    logConfig.value = ProtoBuf.load(LoggerConfig.serializer(), rawConfig.readBytes()!!).freeze()
}

private fun jbyteArray?.readBytes() = this?.let { jbytes ->
    val length = GetArrayLength(jbytes)
    val buffer: COpaquePointer? = GetPrimitiveArrayCritical(jbytes, null)
    try {
        buffer?.readBytes(length)
    } finally {
        ReleasePrimitiveArrayCritical(jbytes, buffer, JNI_ABORT)
    }

}

@Suppress("UNUSED", "UNUSED_PARAMETER")
@CName("Java_com_epam_drill_logger_NativeApi_getConfig")
fun get(env: JNIEnv, thiz: jobject): jbyteArray {
    val rawConfig = ProtoBuf.dump(LoggerConfig.serializer(), logConfig.value)
    val newByteArray: jbyteArray = NewByteArray(rawConfig.size)!!
    SetByteArrayRegion(newByteArray, 0, rawConfig.size, rawConfig.toCValues())
    return newByteArray
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