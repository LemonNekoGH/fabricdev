package moe.lemonneko.fabricdev.module

import com.intellij.ide.util.EditorHelper
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager
import moe.lemonneko.fabricdev.util.BuildSystem
import moe.lemonneko.fabricdev.util.FileTemplateManagerProxy
import moe.lemonneko.fabricdev.util.runWriteTask
import org.apache.commons.lang.StringUtils

class FabricProjectConfiguration {
    var minecraftVersion = ""
    var packageName = ""
    var mainClassName = ""
    var versionName = ""
    var modId = ""
    var modName = ""

    fun create(project: Project, buildSystem: BuildSystem, indicator: ProgressIndicator) {
        if (project.isDisposed) return
        val dirs = buildSystem.directories
        runWriteTask {
            indicator.text = "Writing main class"
            val classPath = dirs.sourceDir.path + "/" + StringUtils.replace(packageName, ".", "/")
            val classDir = VfsUtil.createDirectories(classPath)
            val classFile = classDir.findOrCreateChildData(this, "$mainClassName.java")
            FileTemplateManagerProxy.applyMainClassTemplate(
                project,
                classFile,
                packageName,
                mainClassName
            )
            PsiManager.getInstance(project).findFile(classFile)?.let {
                EditorHelper.openInEditor(it)
            }

            indicator.text = "Writing minxin class"
            val mixinPackage = classDir.createChildDirectory(this,"mixin")
            val mixinClassFile = mixinPackage.findOrCreateChildData(this, "MyMixin.java")
            FileTemplateManagerProxy.applyMixinClassTemplate(project, mixinClassFile, packageName)

            indicator.text = "Generating mod json"
            val resourceDir = dirs.resourceDir
            val modJsonFile = resourceDir.findOrCreateChildData(this, "fabric.mod.json")
            FileTemplateManagerProxy.applyModJsonTemplate(
                project, modJsonFile, modId, versionName, modName, packageName, mainClassName
            )
            val mixinJsonFile = resourceDir.findOrCreateChildData(this,"$modId.mixins.json")
            FileTemplateManagerProxy.applyMixinJsonFile(project,mixinJsonFile,packageName)
        }
    }

}
