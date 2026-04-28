package com.example.myplugin.templates

import com.intellij.icons.AllIcons
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory

class CreateMyPluginServiceAction : CreateFileFromTemplateAction(
    "My Plugin Service",
    "Create a plugin service from a bundled template",
    AllIcons.Nodes.Class,
), DumbAware {

    override fun buildDialog(
        project: Project,
        directory: PsiDirectory,
        builder: CreateFileFromTemplateDialog.Builder,
    ) {
        builder
            .setTitle("New Plugin Service")
            .addKind("My Plugin Service", AllIcons.Nodes.Class, TEMPLATE_NAME)
    }

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String =
        "Create My Plugin Service $newName"

    companion object {
        const val TEMPLATE_NAME = "MyPluginService.kt"
    }
}
