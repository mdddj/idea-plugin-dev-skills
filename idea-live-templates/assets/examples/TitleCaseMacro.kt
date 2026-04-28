package com.example.livetemplates

import com.intellij.codeInsight.template.Expression
import com.intellij.codeInsight.template.ExpressionContext
import com.intellij.codeInsight.template.Result
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.codeInsight.template.TextResult
import com.intellij.codeInsight.template.macro.MacroBase
import com.intellij.openapi.util.text.StringUtil

internal class TitleCaseMacro : MacroBase("titleCase", "titleCase(String)") {

    override fun calculateResult(
        params: Array<out Expression>,
        context: ExpressionContext,
        quick: Boolean
    ): Result? {
        val text = getTextResult(params, context, true) ?: return null
        return TextResult(if (text.isNotEmpty()) StringUtil.toTitleCase(text) else text)
    }

    override fun isAcceptableInContext(context: TemplateContextType): Boolean {
        return context is MarkdownContext
    }
}
