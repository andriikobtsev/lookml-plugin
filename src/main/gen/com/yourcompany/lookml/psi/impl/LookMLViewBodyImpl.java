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

public class LookMLViewBodyImpl extends ASTWrapperPsiElement implements LookMLViewBody {

  public LookMLViewBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitViewBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LookMLDerivedTableProperty> getDerivedTablePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLDerivedTableProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLDimensionDefinition> getDimensionDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLDimensionDefinition.class);
  }

  @Override
  @NotNull
  public List<LookMLDimensionGroupDefinition> getDimensionGroupDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLDimensionGroupDefinition.class);
  }

  @Override
  @NotNull
  public List<LookMLDrillFieldsProperty> getDrillFieldsPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLDrillFieldsProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLFilterDefinition> getFilterDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLFilterDefinition.class);
  }

  @Override
  @NotNull
  public List<LookMLHtmlProperty> getHtmlPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLHtmlProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLMeasureDefinition> getMeasureDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLMeasureDefinition.class);
  }

  @Override
  @NotNull
  public List<LookMLParameterDefinition> getParameterDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLParameterDefinition.class);
  }

  @Override
  @NotNull
  public List<LookMLProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSetDefinition> getSetDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSetDefinition.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlAlwaysFilterProperty> getSqlAlwaysFilterPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlAlwaysFilterProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlAlwaysWhereProperty> getSqlAlwaysWherePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlAlwaysWhereProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlProperty> getSqlPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlTableNameProperty> getSqlTableNamePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlTableNameProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLTestDefinition> getTestDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLTestDefinition.class);
  }

}
