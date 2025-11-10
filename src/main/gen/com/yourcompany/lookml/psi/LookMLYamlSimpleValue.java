// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLYamlSimpleValue extends PsiElement {

  @Nullable
  LookMLBooleanValue getBooleanValue();

  @Nullable
  LookMLFieldPattern getFieldPattern();

  @Nullable
  LookMLQualifiedIdentifier getQualifiedIdentifier();

  @Nullable
  LookMLTemplateExpression getTemplateExpression();

  @Nullable
  LookMLWildcardIdentifier getWildcardIdentifier();

  @Nullable
  LookMLYamlExpressionValue getYamlExpressionValue();

  @Nullable
  LookMLYamlTextLine getYamlTextLine();

  @Nullable
  LookMLYamlValueTokens getYamlValueTokens();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  PsiElement getNumber();

  @Nullable
  PsiElement getString();

}
