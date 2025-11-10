// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLQueryBody extends PsiElement {

  @NotNull
  List<LookMLDimensionsProperty> getDimensionsPropertyList();

  @NotNull
  List<LookMLMeasuresProperty> getMeasuresPropertyList();

  @NotNull
  List<LookMLProperty> getPropertyList();

  @NotNull
  List<LookMLTimezoneProperty> getTimezonePropertyList();

}
