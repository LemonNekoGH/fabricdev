package moe.lemonneko.fabricdev.module.facet

import com.intellij.facet.Facet
import com.intellij.facet.FacetType
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import moe.lemonneko.fabricdev.module.FabricModuleType

class FabricFacetType : FacetType<FabricFacet,FabricFacetConfiguration>(FabricFacet.id,"fabric","Fabric") {
    override fun createFacet(
        module: Module,
        name: String?,
        configuration: FabricFacetConfiguration,
        underlyingFacet: Facet<*>?
    ) = FabricFacet(module,name,configuration,underlyingFacet)

    override fun createDefaultConfiguration() = FabricFacetConfiguration()

    override fun isSuitableModuleType(moduleType: ModuleType<*>?) = moduleType is FabricModuleType
}
