// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLFormParamBody extends PsiElement {

  @NotNull
  List<LookMLDefaultProperty> getDefaultPropertyList();

  @NotNull
  List<LookMLLabelProperty> getLabelPropertyList();

  @NotNull
  List<LookMLNameProperty> getNamePropertyList();

  @NotNull
  List<LookMLProperty> getPropertyList();

  @NotNull
  List<LookMLRequiredProperty> getRequiredPropertyList();

  @NotNull
  List<LookMLTypeProperty> getTypePropertyList();

}
