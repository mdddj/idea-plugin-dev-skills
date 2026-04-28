package com.example.myplugin.templates

import com.intellij.icons.AllIcons
import com.intellij.ide.fileTemplates.FileTemplateDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptor
import com.intellij.ide.fileTemplates.FileTemplateGroupDescriptorFactory

class MyTemplateGroupFactory : FileTemplateGroupDescriptorFactory {
    override fun getFileTemplatesDescriptor(): FileTemplateGroupDescriptor {
        val group = FileTemplateGroupDescriptor("My Plugin", AllIcons.Nodes.Class)
        group.addTemplate(FileTemplateDescriptor("MyPluginInspection.kt", AllIcons.Nodes.Class))
        return group
    }
}
