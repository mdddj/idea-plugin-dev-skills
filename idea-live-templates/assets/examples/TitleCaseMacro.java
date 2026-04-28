package com.example.livetemplates;

import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInsight.template.Result;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.codeInsight.template.TextResult;
import com.intellij.codeInsight.template.macro.MacroBase;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

final class TitleCaseMacro extends MacroBase {

  TitleCaseMacro() {
    super("titleCase", "titleCase(String)");
  }

  private TitleCaseMacro(String name, String description) {
    super(name, description);
  }

  @Override
  protected Result calculateResult(Expression @NotNull [] params, ExpressionContext context, boolean quick) {
    String text = getTextResult(params, context, true);
    if (text == null) {
      return null;
    }
    if (!text.isEmpty()) {
      text = StringUtil.toTitleCase(text);
    }
    return new TextResult(text);
  }

  @Override
  public boolean isAcceptableInContext(TemplateContextType context) {
    return context instanceof MarkdownContext;
  }
}
