// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLYamlContent extends PsiElement {

  @NotNull
  List<LookMLYamlListEntry> getYamlListEntryList();

  @NotNull
  List<LookMLYamlProperty> getYamlPropertyList();

  @NotNull
  List<LookMLYamlTextLine> getYamlTextLineList();

}
