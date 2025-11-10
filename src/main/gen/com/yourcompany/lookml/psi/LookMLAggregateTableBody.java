// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLAggregateTableBody extends PsiElement {

  @NotNull
  List<LookMLMaterializationProperty> getMaterializationPropertyList();

  @NotNull
  List<LookMLProperty> getPropertyList();

  @NotNull
  List<LookMLQueryProperty> getQueryPropertyList();

}
