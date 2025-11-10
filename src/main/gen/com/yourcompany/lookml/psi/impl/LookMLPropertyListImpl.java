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

public class LookMLPropertyListImpl extends ASTWrapperPsiElement implements LookMLPropertyList {

  public LookMLPropertyListImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitPropertyList(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LookMLActionProperty> getActionPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLActionProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLAllowedValueProperty> getAllowedValuePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLAllowedValueProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLCaseProperty> getCasePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLCaseProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLDrillFieldsProperty> getDrillFieldsPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLDrillFieldsProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLFiltersProperty> getFiltersPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLFiltersProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLHtmlProperty> getHtmlPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLHtmlProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLLinkProperty> getLinkPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLLinkProperty.class);
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
  public List<LookMLSqlAlwaysWhereProperty> getSqlAlwaysWherePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlAlwaysWhereProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlLatitudeProperty> getSqlLatitudePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlLatitudeProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlLongitudeProperty> getSqlLongitudePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlLongitudeProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlOnProperty> getSqlOnPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlOnProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSqlProperty> getSqlPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSqlProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLSuggestionsProperty> getSuggestionsPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLSuggestionsProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLTagsProperty> getTagsPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLTagsProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLTiersProperty> getTiersPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLTiersProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLTimeframesProperty> getTimeframesPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLTimeframesProperty.class);
  }

}
