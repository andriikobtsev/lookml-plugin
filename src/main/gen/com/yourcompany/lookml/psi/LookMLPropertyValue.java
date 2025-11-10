// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLPropertyValue extends PsiElement {

  @Nullable
  LookMLArrayValue getArrayValue();

  @Nullable
  LookMLBooleanValue getBooleanValue();

  @Nullable
  LookMLHyphenatedIdentifier getHyphenatedIdentifier();

  @Nullable
  LookMLPropertyList getPropertyList();

  @Nullable
  LookMLPropertyValueExpression getPropertyValueExpression();

  @Nullable
  LookMLTemplateExpression getTemplateExpression();

  @Nullable
  PsiElement getIdentifier();

  @Nullable
  PsiElement getNumber();

  @Nullable
  PsiElement getString();

}
