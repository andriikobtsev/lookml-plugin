// This is a generated file. Not intended for manual editing.
package com.yourcompany.lookml.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LookMLExploreBody extends PsiElement {

  @NotNull
  List<LookMLAccessFilterProperty> getAccessFilterPropertyList();

  @NotNull
  List<LookMLAggregateTableDefinition> getAggregateTableDefinitionList();

  @NotNull
  List<LookMLAlwaysFilterProperty> getAlwaysFilterPropertyList();

  @NotNull
  List<LookMLConditionallyFilterProperty> getConditionallyFilterPropertyList();

  @NotNull
  List<LookMLFromProperty> getFromPropertyList();

  @NotNull
  List<LookMLJoinDefinition> getJoinDefinitionList();

  @NotNull
  List<LookMLProperty> getPropertyList();

  @NotNull
  List<LookMLRequiredAccessGrantsProperty> getRequiredAccessGrantsPropertyList();

  @NotNull
  List<LookMLSqlAlwaysFilterProperty> getSqlAlwaysFilterPropertyList();

  @NotNull
  List<LookMLSqlAlwaysHavingProperty> getSqlAlwaysHavingPropertyList();

  @NotNull
  List<LookMLSqlAlwaysWhereProperty> getSqlAlwaysWherePropertyList();

  @NotNull
  List<LookMLSqlTableNameProperty> getSqlTableNamePropertyList();

  @NotNull
  List<LookMLViewNameProperty> getViewNamePropertyList();

}
