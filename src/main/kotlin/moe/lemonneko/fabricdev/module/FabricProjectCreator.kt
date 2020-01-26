package moe.lemonneko.fabricdev.module

import com.intellij.openapi.module.Module
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.vfs.VirtualFile
import moe.lemonneko.fabricdev.util.BuildSystem
import moe.lemonneko.fabricdev.util.logger

class FabricProjectCreator {
    val config = FabricProjectConfiguration()
    private val buildSystem = BuildSystem()
    fun create(rootDir: VirtualFile, module: Module) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(module.project, "Setting up project...", false) {
            override fun shouldStartInBackground() = false
            override fun run(indicator: ProgressIndicator) {
                if (module.isDisposed || module.project.isDisposed) {
                    return
                }
                indicator.isIndeterminate = true
                buildSystem.create(module.project, rootDir, config, indicator)
                logger.info("")
                config.create(module.project, buildSystem, indicator)
                buildSystem.finishSetup(module,rootDir,config,indicator)
            }
        })
    }
}
