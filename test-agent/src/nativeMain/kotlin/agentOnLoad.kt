@file:Suppress("unused", "FunctionName")

package test

import com.epam.drill.jvmapi.gen.*
import com.epam.drill.kni.*

object Agent : JvmtiAgent {
    override fun agentOnLoad(options: String): Int {
        println("agentOnLoad")
        return JNI_OK
    }

    override fun agentOnUnload() {
        println("agentOnUnload")
    }

}
