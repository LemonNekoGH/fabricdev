package moe.lemonneko.fabricdev.module.facet

import com.intellij.facet.ui.FacetEditorTab
import javax.swing.JComponent
import javax.swing.JPanel

class FabricFacetEditorTab : FacetEditorTab() {
    override fun isModified() = false

    override fun getDisplayName() = "Fabric module settings"

    override fun createComponent(): JComponent {
        return JPanel()
    }
}
