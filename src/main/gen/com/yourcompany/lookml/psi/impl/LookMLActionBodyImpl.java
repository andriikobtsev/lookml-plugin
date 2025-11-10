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

public class LookMLActionBodyImpl extends ASTWrapperPsiElement implements LookMLActionBody {

  public LookMLActionBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitActionBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LookMLFormParamProperty> getFormParamPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLFormParamProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLFormUrlProperty> getFormUrlPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLFormUrlProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLIconUrlProperty> getIconUrlPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLIconUrlProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLParamProperty> getParamPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLParamProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLUrlProperty> getUrlPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLUrlProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLUserAttributeParamProperty> getUserAttributeParamPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLUserAttributeParamProperty.class);
  }

}
