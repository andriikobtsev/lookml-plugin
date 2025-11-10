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

public class LookMLCaseBodyImpl extends ASTWrapperPsiElement implements LookMLCaseBody {

  public LookMLCaseBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitCaseBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LookMLElseClause> getElseClauseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLElseClause.class);
  }

  @Override
  @NotNull
  public List<LookMLWhenClause> getWhenClauseList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLWhenClause.class);
  }

}
