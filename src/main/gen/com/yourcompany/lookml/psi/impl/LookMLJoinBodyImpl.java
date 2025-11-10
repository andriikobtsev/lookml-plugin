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

public class LookMLJoinBodyImpl extends ASTWrapperPsiElement implements LookMLJoinBody {

  public LookMLJoinBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitJoinBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LookMLFieldsProperty> getFieldsPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLFieldsProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLFromProperty> getFromPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLFromProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLRequiredJoinsProperty> getRequiredJoinsPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLRequiredJoinsProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlAlwaysWhereProperty> getSqlAlwaysWherePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlAlwaysWhereProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlOnProperty> getSqlOnPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlOnProperty.class);
  }

}
