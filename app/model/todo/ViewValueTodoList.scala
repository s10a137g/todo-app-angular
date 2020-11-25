package model.todo

import model.ViewValueCommon

case class ViewValueTodoList(
  title:    String,
  cssSrc:   Seq[String],
  jsSrc:    Seq[String],
  todoList: Seq[ViewValueTodo]
) extends ViewValueCommon
