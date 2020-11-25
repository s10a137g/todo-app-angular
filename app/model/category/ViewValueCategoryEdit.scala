package model.category

import model.ViewValueCommon
import lib.persistence.onMySQL.CategoryRepository

case class ViewValueCategoryEdit(
  title:    String,
  cssSrc:   Seq[String],
  jsSrc:    Seq[String]
) extends ViewValueCommon
