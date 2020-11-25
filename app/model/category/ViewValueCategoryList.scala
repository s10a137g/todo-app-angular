package model.category

import model.ViewValueCommon

case class ViewValueCategoryList(
  title:    String,
  cssSrc:   Seq[String],
  jsSrc:    Seq[String],
  categoryList: Seq[ViewValueCategory]
) extends ViewValueCommon
