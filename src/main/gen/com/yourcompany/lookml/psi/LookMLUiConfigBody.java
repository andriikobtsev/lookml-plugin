// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLUiConfigBody extends PsiElement {

  @NotNull
  List<LookMLDisplayProperty> getDisplayPropertyList();

  @NotNull
  List<LookMLOptionsProperty> getOptionsPropertyList();

  @NotNull
  List<LookMLProperty> getPropertyList();

  @NotNull
  List<LookMLTypeProperty> getTypePropertyList();

}
