// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLJoinBody extends PsiElement {

  @NotNull
  List<LookMLFieldsProperty> getFieldsPropertyList();

  @NotNull
  List<LookMLFromProperty> getFromPropertyList();

  @NotNull
  List<LookMLProperty> getPropertyList();

  @NotNull
  List<LookMLRequiredJoinsProperty> getRequiredJoinsPropertyList();

  @NotNull
  List<LookMLSqlAlwaysWhereProperty> getSqlAlwaysWherePropertyList();

  @NotNull
  List<LookMLSqlOnProperty> getSqlOnPropertyList();

}
