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

public class LookMLConditionalFormatBodyImpl extends ASTWrapperPsiElement implements LookMLConditionalFormatBody {

  public LookMLConditionalFormatBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitConditionalFormatBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LookMLBackgroundColorProperty> getBackgroundColorPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLBackgroundColorProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLBoldProperty> getBoldPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLBoldProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLFontColorProperty> getFontColorPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLFontColorProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLTypeProperty> getTypePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLTypeProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLValueProperty> getValuePropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLValueProperty.class);
  }

}
