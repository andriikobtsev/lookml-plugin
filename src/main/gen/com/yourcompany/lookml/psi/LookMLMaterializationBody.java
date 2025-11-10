// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLMaterializationBody extends PsiElement {

  @NotNull
  List<LookMLClusterKeysProperty> getClusterKeysPropertyList();

  @NotNull
  List<LookMLDatagroupTriggerProperty> getDatagroupTriggerPropertyList();

  @NotNull
  List<LookMLIncrementKeyProperty> getIncrementKeyPropertyList();

  @NotNull
  List<LookMLIncrementOffsetProperty> getIncrementOffsetPropertyList();

  @NotNull
  List<LookMLPartitionKeysProperty> getPartitionKeysPropertyList();

  @NotNull
  List<LookMLProperty> getPropertyList();

}
