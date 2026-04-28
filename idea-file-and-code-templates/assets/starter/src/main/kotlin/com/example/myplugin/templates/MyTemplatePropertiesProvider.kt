package com.example.myplugin.templates

import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiDirectory
import java.util.Properties

class MyTemplatePropertiesProvider : DefaultTemplatePropertiesProvider {
    override fun fillProperties(directory: PsiDirectory, props: Properties) {
        val packageName = JavaDirectoryService.getInstance().getPackage(directory)?.qualifiedName.orEmpty()
        props.setProperty("PACKAGE_NAME", packageName)
        props.setProperty("PLUGIN_ID", "com.example.myplugin")
    }
}
