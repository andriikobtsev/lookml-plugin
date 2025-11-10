// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.yourcompany.lookml.psi.LookMLTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.yourcompany.lookml.psi.*;

public class LookMLYamlItemContentImpl extends ASTWrapperPsiElement implements LookMLYamlItemContent {

  public LookMLYamlItemContentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitYamlItemContent(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LookMLYamlFlowValue getYamlFlowValue() {
    return findChildByClass(LookMLYamlFlowValue.class);
  }

  @Override
  @Nullable
  public LookMLYamlProperty getYamlProperty() {
    return findChildByClass(LookMLYamlProperty.class);
  }

  @Override
  @Nullable
  public LookMLYamlSimpleValue getYamlSimpleValue() {
    return findChildByClass(LookMLYamlSimpleValue.class);
  }

}
