package moe.lemonneko.fabricdev.util

import com.intellij.execution.RunManager
import com.intellij.openapi.externalSystem.service.execution.ExternalSystemRunConfiguration
import com.intellij.openapi.module.Module
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import moe.lemonneko.fabricdev.module.FabricProjectConfiguration
import org.apache.commons.io.FileUtils
import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProgressListener
import org.jetbrains.plugins.gradle.service.execution.GradleExternalTaskConfigurationType
import org.jetbrains.plugins.gradle.service.project.open.linkAndRefreshGradleProject
import org.jetbrains.plugins.gradle.util.GradleConstants
import java.io.File

class BuildSystem {
    lateinit var directories: DirectorySet

    data class DirectorySet(
        val sourceDir: VirtualFile,
        val resourceDir: VirtualFile,
        val testSourceDir: VirtualFile,
        val testResourceDir: VirtualFile
    )

    fun create(
        project: Project,
        rootDirectory: VirtualFile,
        configuration: FabricProjectConfiguration,
        indicator: ProgressIndicator
    ) {
        if (project.isDisposed) return
        rootDirectory.refresh(false, true)
        directories = createDirectories(rootDirectory)
        val descriptor =
            ProjectDescriptor(rootDirectory, project)
        handleCreate(descriptor, configuration, indicator)
    }

    private fun handleCreate(
        descriptor: ProjectDescriptor,
        config: FabricProjectConfiguration,
        indicator: ProgressIndicator
    ) {
        val (rootDir, project) = descriptor
        runWriteTask {
            indicator.isIndeterminate = true
            indicator.text = "Generating gradle files"
            val (buildGradle, settingsGradle) = setupGradleFiles(rootDir)
            FileTemplateManagerProxy.applyBuildGradleTemplate(
                project,
                buildGradle,
                config.packageName,
                config.versionName
            )
            FileTemplateManagerProxy.applySettingsGradleTemplate(project, settingsGradle)
        }
        setupWrapper(descriptor, indicator)
        runGradleTask(descriptor, indicator) {
            it.forTasks("genSources")
        }
    }

    private fun setupGradleFiles(dir: VirtualFile): GradleFiles {
        return GradleFiles(
            dir.findOrCreateChildData(this, "build.gradle"),
            dir.findOrCreateChildData(this, "settings.gradle")
        )
    }

    private fun createDirectories(rootDirectory: VirtualFile): DirectorySet {
        val mainJava = VfsUtil.createDirectories(rootDirectory.path + "/src/main/java")
        val mainResources = VfsUtil.createDirectories(rootDirectory.path + "/src/main/resources")
        val testJava = VfsUtil.createDirectories(rootDirectory.path + "/src/test/java")
        val testResources = VfsUtil.createDirectories(rootDirectory.path + "/src/test/resources")
        return DirectorySet(
            mainJava,
            mainResources,
            testJava,
            testResources
        )
    }

    private fun setupWrapper(descriptor: ProjectDescriptor, indicator: ProgressIndicator) {
        runWriteTask {
            val wrapperDirPath = VfsUtil.createDirectoryIfMissing(descriptor.rootDirectory, "gradle/wrapper").path
            FileUtils.writeLines(
                File(wrapperDirPath, "gradle-wrapper.properties"),
                listOf("distributionUrl=https\\://services.gradle.org/distributions/gradle-5.2.1-all.zip")
            )
        }
        runGradleTask(descriptor, indicator) {
            it.forTasks("wrapper")
        }
    }

    private inline fun runGradleTask(
        descriptor: ProjectDescriptor,
        indicator: ProgressIndicator,
        func: (BuildLauncher) -> Unit
    ) {
        val rootDir = descriptor.rootDirectory
        val connector = GradleConnector.newConnector()
        connector.forProjectDirectory(rootDir.localFile)
        val connection = connector.connect()
        val launcher = connection.newBuild()
        connection.use {
            launcher.addProgressListener(ProgressListener { event ->
                indicator.text = event.description
            })
            func(launcher)
            launcher.run()
        }
    }

    fun finishSetup(
        module: Module,
        rootDir: VirtualFile,
        config: FabricProjectConfiguration,
        indicator: ProgressIndicator
    ) {
        val project = module.project
        if (module.isDisposed || project.isDisposed) return

        linkAndRefreshGradleProject(rootDir.path,project)
        invokeLater {
            val gradleType = GradleExternalTaskConfigurationType.getInstance()
            val runManager = RunManager.getInstance(project)
            val runConfiguration = ExternalSystemRunConfiguration(
                GradleConstants.SYSTEM_ID,
                project,
                gradleType.configurationFactories[0],
                module.name + "build"
            )
            runConfiguration.settings.externalProjectPath = rootDir.path
            runConfiguration.settings.executionName = module.name + "build"
            runConfiguration.settings.taskNames = listOf("build")
            runConfiguration.isAllowRunningInParallel = false
            val gradleBuildSettings = runManager.createConfiguration(
                runConfiguration,
                GradleExternalTaskConfigurationType.getInstance().configurationFactories.first()
            )
            gradleBuildSettings.isActivateToolWindowBeforeRun = true
            runManager.addConfiguration(gradleBuildSettings, false)
            if (runManager.selectedConfiguration == null) {
                runManager.selectedConfiguration = gradleBuildSettings
            }
        }
    }

    data class GradleFiles(val buildGradle: VirtualFile, val buildProperties: VirtualFile)

    data class ProjectDescriptor(val rootDirectory: VirtualFile, val project: Project)
}
