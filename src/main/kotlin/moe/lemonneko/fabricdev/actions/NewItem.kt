package moe.lemonneko.fabricdev.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import moe.lemonneko.fabricdev.util.logger

class NewItem : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val projectPath = project?.projectFile?.parent?.path ?: return
        val dataContext = e.dataContext
        logger.info(dataContext::class.java.name)
    }
}