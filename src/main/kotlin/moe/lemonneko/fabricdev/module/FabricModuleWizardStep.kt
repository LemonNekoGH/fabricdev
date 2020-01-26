package moe.lemonneko.fabricdev.module

import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.openapi.ui.Messages
import moe.lemonneko.fabricdev.util.NullValueInModuleWizardException

class FabricModuleWizardStep(private val creator: FabricProjectCreator) : ModuleWizardStep() {
    private val stepInterface = FabricModuleWizardPanel()

    override fun updateStep() {
        val packageName = stepInterface.packageNameField.text
        val mainClass = stepInterface.modMainClassField.text
        val versionName = stepInterface.versionField.text
        val selectedVersion = stepInterface.versionSelector.selectedItem
        val modName = stepInterface.modNameField.text
        val modId = stepInterface.modIdField.text
        if (packageName == null || packageName == "") {
            Messages.showDialog("Mod的包名不能为空", "错误", arrayOf("好的"), 0, null)
            throw Exception("Mod的包名不能为空")
        }
        if (mainClass == null || mainClass == "") {
            Messages.showDialog("Mod的主类名不能为空", "错误", arrayOf("好的"), 0, null)
            throw NullValueInModuleWizardException("Mod的主类名不能为空")
        }
        if (selectedVersion == null || selectedVersion == "") {
            Messages.showDialog("获取Minecraft版本时出现错误，请将此信息发送给插件作者", "错误", arrayOf("好的"), 0, null)
            throw NullValueInModuleWizardException("获取Minecraft版本时出现错误，请将此信息发送给插件作者")
        }
        if (versionName == null || versionName == "") {
            Messages.showDialog("Mod版本不能为空", "错误", arrayOf("好的"), 0, null)
            throw NullValueInModuleWizardException("Mod版本不能为空")
        }
        if (modName == null || modName == "") {
            Messages.showDialog("Mod名字不能为空", "错误", arrayOf("好的"), 0, null)
            throw NullValueInModuleWizardException("Mod名字不能为空")
        }
        if (modId == null || modId == "") {
            Messages.showDialog("modid不能为空", "错误", arrayOf("好的"), 0, null)
            throw NullValueInModuleWizardException("modid名字不能为空")
        }
    }

    override fun updateDataModel() {
        val packageName = stepInterface.packageNameField.text
        val mainClass = stepInterface.modMainClassField.text
        val versionName = stepInterface.versionField.text
        val selectedVersion = stepInterface.versionSelector.selectedItem
        val modName = stepInterface.modNameField.text
        val modId = stepInterface.modIdField.text
        creator.config.minecraftVersion = selectedVersion!!.toString()
        creator.config.packageName = packageName
        creator.config.mainClassName = mainClass
        creator.config.versionName = versionName
        creator.config.modName = modName
        creator.config.modId = modId
    }

    override fun getComponent() = stepInterface
}
