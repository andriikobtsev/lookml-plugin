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

public class LookMLMaterializationBodyImpl extends ASTWrapperPsiElement implements LookMLMaterializationBody {

  public LookMLMaterializationBodyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LookMLVisitor visitor) {
    visitor.visitMaterializationBody(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LookMLVisitor) accept((LookMLVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<LookMLClusterKeysProperty> getClusterKeysPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLClusterKeysProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLDatagroupTriggerProperty> getDatagroupTriggerPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLDatagroupTriggerProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLIncrementKeyProperty> getIncrementKeyPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLIncrementKeyProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLIncrementOffsetProperty> getIncrementOffsetPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLIncrementOffsetProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLPartitionKeysProperty> getPartitionKeysPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLPartitionKeysProperty.class);
  }

  @Override
  @NotNull
  public List<LookMLProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, LookMLProperty.class);
  }

}
