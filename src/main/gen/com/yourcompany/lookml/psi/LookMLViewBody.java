// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLViewBody extends PsiElement {

  @NotNull
  List<LookMLDerivedTableProperty> getDerivedTablePropertyList();

  @NotNull
  List<LookMLDimensionDefinition> getDimensionDefinitionList();

  @NotNull
  List<LookMLDimensionGroupDefinition> getDimensionGroupDefinitionList();

  @NotNull
  List<LookMLDrillFieldsProperty> getDrillFieldsPropertyList();

  @NotNull
  List<LookMLFilterDefinition> getFilterDefinitionList();

  @NotNull
  List<LookMLHtmlProperty> getHtmlPropertyList();

  @NotNull
  List<LookMLMeasureDefinition> getMeasureDefinitionList();

  @NotNull
  List<LookMLParameterDefinition> getParameterDefinitionList();

  @NotNull
  List<LookMLProperty> getPropertyList();

  @NotNull
  List<LookMLSetDefinition> getSetDefinitionList();

  @NotNull
  List<LookMLSqlAlwaysFilterProperty> getSqlAlwaysFilterPropertyList();

  @NotNull
  List<LookMLSqlAlwaysWhereProperty> getSqlAlwaysWherePropertyList();

  @NotNull
  List<LookMLSqlProperty> getSqlPropertyList();

  @NotNull
  List<LookMLSqlTableNameProperty> getSqlTableNamePropertyList();

  @NotNull
  List<LookMLTestDefinition> getTestDefinitionList();

}
