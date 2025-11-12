---
- dashboard: test_compound_identifiers
  title: "Test Compound Identifiers"
  description: |
    This file tests the fix for compound identifiers with dots.
    Previously, "dashboard" in file paths caused parsing errors.
  
  # Test cases that should now work:
  
  # File path with dashboard in the name
  __FILE: condo-dwh/dashboards/external/test_explore/investor_overview_v2.dashboard.lookml
  
  # Property with compound identifier value  
  source_file: reports/analytics.dashboard.lkml
  backup_location: archive/old.dashboard.backup.lkml
  
  elements:
  - title: Test Element
    name: test_element
    type: single_value
    
    # These should also work fine:
    model: my_model.dashboard.view
    explore: data.dashboard.explore
    
    # Field references with dashboard in the name
    fields: [
      transactions.dashboard_id,
      reports.dashboard.status,
      file.dashboard.type
    ]
    
    row: 0
    col: 0
    width: 12
    height: 6
