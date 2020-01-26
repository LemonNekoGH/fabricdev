package moe.lemonneko.fabricdev.module

import com.intellij.ide.util.projectWizard.JavaModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.DumbAwareRunnable
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class FabricModuleBuilder : JavaModuleBuilder() {
    private val creator = FabricProjectCreator()
    override fun setupRootModel(modifiableRootModel: ModifiableRootModel) {
        val project = modifiableRootModel.project
        val root = createAndGetRoot() ?: return
        modifiableRootModel.addContentEntry(root)
        if (moduleJdk != null) {
            modifiableRootModel.sdk = moduleJdk
        }
        val r = DumbAwareRunnable {
            creator.create(root, modifiableRootModel.module)
        }
        if (project.isDisposed) {
            return
        }
        if (ApplicationManager.getApplication().isUnitTestMode || ApplicationManager.getApplication().isHeadlessEnvironment) {
            r.run()
            return
        }
        if (!project.isInitialized) {
            StartupManager.getInstance(project).registerPostStartupActivity(r)
            return
        }
        DumbService.getInstance(project).runWhenSmart(r)
    }

    override fun getModuleType() = FabricModuleType()
    lateinit var minecraftVersion: String

    override fun createWizardSteps(
        wizardContext: WizardContext,
        modulesProvider: ModulesProvider
    ): Array<ModuleWizardStep> {
        return arrayOf(FabricModuleWizardStep(creator))
    }

    private fun createAndGetRoot(): VirtualFile? {
        val temp = contentEntryPath ?: return null
        val path = FileUtil.toSystemIndependentName(temp)
        return try {
            Files.createDirectories(Paths.get(path))
            LocalFileSystem.getInstance().refreshAndFindFileByPath(path)
        } catch (e: IOException) {
            null
        }
    }
}
