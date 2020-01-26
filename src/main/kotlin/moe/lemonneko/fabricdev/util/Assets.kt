package moe.lemonneko.fabricdev.util

import com.intellij.openapi.util.IconLoader

object Assets {
    object Icons{
        val ICON = loadIcon("/assets/icons/Fabric.png")
        val ICON_2X = loadIcon("/assets/icons/Fabric@2x.png")
    }
    private fun loadIcon(path: String) = IconLoader.findIcon(path, this::class.java)!!
}
