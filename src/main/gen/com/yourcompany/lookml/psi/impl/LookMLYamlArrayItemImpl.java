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

public class LookMLYamlArrayItemImpl extends ASTWrapperPsiElement implements LookMLYamlArrayItem {

  public LookMLYamlArrayItemImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitYamlArrayItem(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LookMLFieldPattern getFieldPattern() {
    return findChildByClass(LookMLFieldPattern.class);
  }

  @Override
  @Nullable
  public LookMLSortSpecification getSortSpecification() {
    return findChildByClass(LookMLSortSpecification.class);
  }

  @Override
  @Nullable
  public LookMLWildcardIdentifier getWildcardIdentifier() {
    return findChildByClass(LookMLWildcardIdentifier.class);
  }

  @Override
  @Nullable
  public LookMLYamlFlowObject getYamlFlowObject() {
    return findChildByClass(LookMLYamlFlowObject.class);
  }

  @Override
  @Nullable
  public LookMLYamlSimpleValue getYamlSimpleValue() {
    return findChildByClass(LookMLYamlSimpleValue.class);
  }

}
