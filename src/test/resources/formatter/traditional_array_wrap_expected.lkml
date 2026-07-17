# Test: long array wraps one element per line - EXPECTED (2-space indent, ] at property indent)
view: estates {
  set: estate_month_detail {
    fields: [
      mart__estates_with_attributes.condo_uuid_with_link,
      rentable_unit_id,
      unit_type_grouped,
      apartment_type,
      month_date,
      living_space,
      is_vacant
    ]
  }
}
