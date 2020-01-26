package moe.lemonneko.fabricdev.module

import com.intellij.ui.CollectionComboBoxModel
import java.awt.BorderLayout
import javax.swing.JComboBox
import javax.swing.JPanel
import javax.swing.JTextField

class FabricModuleWizardPanel : JPanel(BorderLayout()) {
    private lateinit var rootPanel: JPanel
    lateinit var versionSelector: JComboBox<String>
    lateinit var packageNameField: JTextField
    lateinit var modMainClassField: JTextField
    lateinit var versionField: JTextField
    lateinit var modNameField: JTextField
    lateinit var modIdField: JTextField

    init {
        add(rootPanel)
        versionSelector.model = CollectionComboBoxModel<String>(listOf(
            "1.14.2",
            "1.15.2"
        ))
        versionSelector.selectedIndex = versionSelector.model.size - 1
        versionField.text = "1.0-SNAPSHOT"
        packageNameField.text = "com.example.examplemod"
        modMainClassField.text = "ExampleMod"
        modNameField.text = "ExampleMod"
        modIdField.text = "examplemod"
    }
}
