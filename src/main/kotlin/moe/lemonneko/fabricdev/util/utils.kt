package moe.lemonneko.fabricdev.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.util.Computable
import com.intellij.openapi.util.Ref
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.util.*
import java.util.logging.Logger

inline fun <T : Any?> runWriteTask(crossinline func: () -> T): T {
    return invokeAndWait<T> {
        ApplicationManager.getApplication().runWriteAction(Computable { func() })
    }
}

fun <T : Any?> invokeAndWait(func: () -> T): T {
    val ref = Ref<T>()
    ApplicationManager.getApplication().invokeAndWait({ ref.set(func()) }, ModalityState.defaultModalityState())
    return ref.get()
}

val Any.logger
    get() = Logger.getLogger(this::class.java.name)

operator fun Properties.set(key: String, value: String) {
    setProperty(key, value)
}

val VirtualFile.localFile: File
    get() = VfsUtilCore.virtualToIoFile(this)

fun invokeLater(func: () -> Unit) {
    ApplicationManager.getApplication().invokeLater(func, ModalityState.defaultModalityState())
}

fun createProperties(map: Map<String, String>): Properties {
    val properties = Properties()
    map.forEach { (t, u) ->
        properties[t] = u
    }
    return properties
}
