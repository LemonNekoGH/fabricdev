package moe.lemonneko.fabricdev.module.facet

import com.intellij.facet.FacetConfiguration
import com.intellij.facet.ui.FacetEditorContext
import com.intellij.facet.ui.FacetValidatorsManager

class FabricFacetConfiguration : FacetConfiguration {
    override fun createEditorTabs(
        editorContext: FacetEditorContext?,
        validatorsManager: FacetValidatorsManager?
    ) = arrayOf(FabricFacetEditorTab())
}
