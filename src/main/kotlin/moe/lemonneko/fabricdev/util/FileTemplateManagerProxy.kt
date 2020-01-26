package moe.lemonneko.fabricdev.util

import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.util.*

object FileTemplateManagerProxy {
    fun applyMainClassTemplate(project: Project, classFile: VirtualFile, packageName: String, className: String) {
        val templateManager = FileTemplateManager.getInstance(project)
        val template = templateManager.getJ2eeTemplate("main_class.java")
        val properties = createProperties(mapOf(
            "PACKAGE_NAME" to packageName,
            "MAIN_CLASS_NAME" to className
        ))
        applyTemplate(
            templateManager,
            properties,
            template,
            classFile
        )
    }

    fun applyBuildGradleTemplate(project: Project, buildFile: VirtualFile, groupId: String, versionName: String) {
        val templateManager = FileTemplateManager.getInstance(project)
        val template = templateManager.getJ2eeTemplate("build.gradle")
        val properties = createProperties(mapOf(
            "VERSION" to versionName,
            "GROUP_ID" to groupId
        ))
        applyTemplate(
            templateManager,
            properties,
            template,
            buildFile
        )
    }

    fun applySettingsGradleTemplate(project: Project, settingsFile: VirtualFile) {
        val templateManager = FileTemplateManager.getInstance(project)
        val template = templateManager.getJ2eeTemplate("settings.gradle")
        applyTemplate(
            templateManager,
            Properties(),
            template,
            settingsFile
        )
    }

    fun applyMixinClassTemplate(project: Project, mixinClassFile: VirtualFile, packageName: String) {
        val templateManager = FileTemplateManager.getInstance(project)
        val template = templateManager.getJ2eeTemplate("mixin_class.java")
        val properties = createProperties(mapOf("PACKAGE_NAME" to packageName))
        applyTemplate(
            templateManager,
            properties,
            template,
            mixinClassFile
        )
    }

    fun applyModJsonTemplate(
        project: Project,
        jsonFile: VirtualFile,
        modId: String,
        version: String,
        modName: String,
        packageName: String,
        mainClassName: String
    ) {
        val fileTemplateManager = FileTemplateManager.getInstance(project)
        val properties = createProperties(mapOf(
            "MOD_ID" to modId,
            "VERSION" to version,
            "MOD_NAME" to modName,
            "PACKAGE_NAME" to packageName,
            "MAIN_CLASS_NAME" to mainClassName
        ))
        val fileTemplate = fileTemplateManager.getJ2eeTemplate("fabric.mod.json")
        applyTemplate(fileTemplateManager,properties,fileTemplate,jsonFile)
    }

    fun applyMixinJsonFile(project: Project,jsonFile: VirtualFile,packageName: String){
        val fileTemplateManager = FileTemplateManager.getInstance(project)
        val properties = createProperties(mapOf("PACKAGE_NAME" to packageName))
        val fileTemplate = fileTemplateManager.getJ2eeTemplate("modid.mixins.json")
        applyTemplate(fileTemplateManager,properties,fileTemplate,jsonFile)
    }

    private fun applyTemplate(
        templateManager: FileTemplateManager,
        properties: Properties,
        template: FileTemplate,
        file: VirtualFile
    ) {
        val allProperties = templateManager.defaultProperties
        properties.let { prop -> allProperties.putAll(prop) }
        val text = template.getText(properties)
        VfsUtil.saveText(file, text)
    }
}
