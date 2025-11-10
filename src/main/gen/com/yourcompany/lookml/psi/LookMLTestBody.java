// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLTestBody extends PsiElement {

  @NotNull
  List<LookMLAssertProperty> getAssertPropertyList();

  @NotNull
  List<LookMLExploreSourceProperty> getExploreSourcePropertyList();

  @NotNull
  List<LookMLProperty> getPropertyList();

}
