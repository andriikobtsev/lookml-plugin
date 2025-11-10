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

public class LookMLDashboardFilterBodyImpl extends ASTWrapperPsiElement implements LookMLDashboardFilterBody {

  public LookMLDashboardFilterBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitDashboardFilterBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LookMLAllowMultipleValuesProperty> getAllowMultipleValuesPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLAllowMultipleValuesProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLDefaultValueProperty> getDefaultValuePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLDefaultValueProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLNameProperty> getNamePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLNameProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLRequiredProperty> getRequiredPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLRequiredProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLTitleProperty> getTitlePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLTitleProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLTypeProperty> getTypePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLTypeProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLUiConfigProperty> getUiConfigPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLUiConfigProperty.class);
  }

}
