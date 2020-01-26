package moe.lemonneko.fabricdev.module.facet

import com.intellij.facet.Facet
import com.intellij.facet.FacetTypeId
import com.intellij.openapi.module.Module

class FabricFacet(
    module: Module,
    name: String?,
    config: FabricFacetConfiguration,
    underlyingFacet: Facet<*>?
) : Facet<FabricFacetConfiguration>(
    FabricFacetType(),
    module,
    name ?: "",
    config,
    underlyingFacet
) {
    companion object {
        val id = FacetTypeId<FabricFacet>("Fabric facet")
    }
}
