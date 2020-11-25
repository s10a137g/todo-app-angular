package model.todo

import lib.model.Todo.Status
import lib.model.Todo
import lib.model.Category
import lib.model.Category.Color
import lib.persistence.onMySQL.CategoryRepository

case class ViewValueTodo(
  id:            Long,
  title:         String,
  body:          String,
  statusName:    String,
  categoryName:  String,
  categoryColor: String
)
