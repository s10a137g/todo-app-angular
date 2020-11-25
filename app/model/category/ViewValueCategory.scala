package model.category

import lib.model.Category.Color

case class ViewValueCategory(
  id:    Long,
  name:  String,
  slug:  String,
  color: Color
)
