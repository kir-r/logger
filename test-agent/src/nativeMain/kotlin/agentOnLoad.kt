@file:Suppress("unused", "FunctionName")

import com.epam.drill.jvmapi.*
import com.epam.drill.jvmapi.gen.*
import kotlinx.cinterop.*
import kotlin.native.concurrent.*

@Suppress("UNUSED_PARAMETER", "UNUSED")
@CName("Agent_OnLoad")
fun agentOnLoad(vmPointer: CPointer<JavaVMVar>, options: String, reservedPtr: Long): Int = memScoped {
    initAgentGlobals(vmPointer)
    return 0
}

@CName("currentEnvs")
fun currentEnvs(): JNIEnvPointer {
    return com.epam.drill.jvmapi.currentEnvs()
}

@CName("jvmtii")
fun jvmtii(): CPointer<jvmtiEnvVar>? {
    return com.epam.drill.jvmapi.jvmtii()
}

@CName("getJvm")
fun getJvm(): CPointer<JavaVMVar>? {
    return vmGlobal.value
}

@CName("JNI_OnUnload")
fun JNI_OnUnload() {
}

@CName("JNI_GetCreatedJavaVMs")
fun JNI_GetCreatedJavaVMs() {
}

@CName("JNI_CreateJavaVM")
fun JNI_CreateJavaVM() {
}

@CName("JNI_GetDefaultJavaVMInitArgs")
fun JNI_GetDefaultJavaVMInitArgs() {
}

@CName("checkEx")
fun checkEx(errCode: jvmtiError, funName: String): jvmtiError {
    return com.epam.drill.jvmapi.checkEx(errCode, funName)
}

private fun MemScope.getJvmti(vm: JavaVMVar): CPointerVar<jvmtiEnvVar> {
    val jvmtiEnvPtr = alloc<CPointerVar<jvmtiEnvVar>>()
    vm.value!!.pointed.GetEnv!!(vm.ptr, jvmtiEnvPtr.ptr.reinterpret(), JVMTI_VERSION.convert())
    return jvmtiEnvPtr
}

private fun MemScope.initAgentGlobals(vmPointer: CPointer<JavaVMVar>) {
    vmGlobal.value = vmPointer.freeze()
    jvmti.value = getJvmti(vmPointer.pointed).value.freeze()
}
