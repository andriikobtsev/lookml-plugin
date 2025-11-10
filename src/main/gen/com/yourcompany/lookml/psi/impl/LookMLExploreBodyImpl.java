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

public class LookMLExploreBodyImpl extends ASTWrapperPsiElement implements LookMLExploreBody {

  public LookMLExploreBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitExploreBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LookMLAccessFilterProperty> getAccessFilterPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLAccessFilterProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLAggregateTableDefinition> getAggregateTableDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLAggregateTableDefinition.class);
  }

  @Override
  @NotNull
  public List<LookMLAlwaysFilterProperty> getAlwaysFilterPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLAlwaysFilterProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLConditionallyFilterProperty> getConditionallyFilterPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLConditionallyFilterProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLFromProperty> getFromPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLFromProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLJoinDefinition> getJoinDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLJoinDefinition.class);
  }

  @Override
  @NotNull
  public List<LookMLProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLRequiredAccessGrantsProperty> getRequiredAccessGrantsPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLRequiredAccessGrantsProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlAlwaysFilterProperty> getSqlAlwaysFilterPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlAlwaysFilterProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlAlwaysHavingProperty> getSqlAlwaysHavingPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlAlwaysHavingProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlAlwaysWhereProperty> getSqlAlwaysWherePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlAlwaysWhereProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlTableNameProperty> getSqlTableNamePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlTableNameProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLViewNameProperty> getViewNamePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLViewNameProperty.class);
  }

}
