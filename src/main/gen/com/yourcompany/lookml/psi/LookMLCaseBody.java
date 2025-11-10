// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLCaseBody extends PsiElement {

  @NotNull
  List<LookMLElseClause> getElseClauseList();

  @NotNull
  List<LookMLWhenClause> getWhenClauseList();

}
