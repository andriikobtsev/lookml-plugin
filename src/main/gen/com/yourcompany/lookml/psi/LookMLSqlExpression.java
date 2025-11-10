// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLSqlExpression extends PsiElement {

  @NotNull
  List<LookMLSqlExpression> getSqlExpressionList();

  @Nullable
  LookMLSqlFieldReference getSqlFieldReference();

  @Nullable
  LookMLTemplateExpression getTemplateExpression();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  PsiElement getNumber();

  @Nullable
  PsiElement getSqlContentToken();

  @Nullable
  PsiElement getString();

}
