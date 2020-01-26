package moe.lemonneko.fabricdev.module

import com.intellij.openapi.module.JavaModuleType
import com.intellij.util.Icons
import moe.lemonneko.fabricdev.util.Assets
import javax.swing.Icon

class FabricModuleType : JavaModuleType(id) {
    override fun createModuleBuilder() = FabricModuleBuilder()

    override fun getName() = "Fabric"

    override fun getDescription() = "Create new fabric module"

    override fun getNodeIcon(isOpened: Boolean) = Assets.Icons.ICON

    companion object{
        val id = "FABRIC_MODULE"
    }
}
