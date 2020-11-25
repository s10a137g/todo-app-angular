package model.todo

import model.ViewValueCommon
import lib.model.Category
import lib.persistence.onMySQL.CategoryRepository

case class ViewValueTodoEdit(
  title:    String,
  cssSrc:   Seq[String],
  jsSrc:    Seq[String],
  categoryList: Seq[CategoryRepository.EntityEmbeddedId]
) extends ViewValueCommon
