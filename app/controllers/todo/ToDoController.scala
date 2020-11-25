/**
  * to do sample project
  */

package controllers.todo

import javax.inject._
import play.api.mvc._

import lib.persistence.onMySQL.{CategoryRepository, TodoRepository}

import model.ViewValueHome
import model.todo.{ViewValueTodo, ViewValueTodoEdit, ViewValueTodoList}
import lib.model.Todo
import slick.jdbc.JdbcProfile
import ixias.persistence.SlickRepository
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import play.api.data._
import play.api.data.Forms._
import play.api.i18n.I18nSupport

case class TodoInsertFormData(title: String, body: String, categoryId: Long)

case class TodoUpdateFormData(
  id:         Long,
  title:      String,
  body:       String,
  status:     Short,
  categoryId: Long
)

@Singleton
class TodoController @Inject() (val controllerComponents: ControllerComponents)
  extends BaseController
     with I18nSupport {

  val todoInsertForm = Form(
    mapping(
      "title"      -> nonEmptyText,
      "body"       -> nonEmptyText,
      "categoryId" -> longNumber
    )(TodoInsertFormData.apply)(TodoInsertFormData.unapply)
  )

  val todoUpdateForm = Form(
    mapping(
      "id"         -> longNumber,
      "title"      -> nonEmptyText,
      "body"       -> nonEmptyText,
      "status"     -> shortNumber,
      "categoryId" -> longNumber
    )(TodoUpdateFormData.apply)(TodoUpdateFormData.unapply)
  )

  val defaultVv = ViewValueHome(
    title  = "Category List",
    cssSrc = Seq("main.css"),
    jsSrc  = Seq("main.js")
  )

  def list() = Action.async { implicit req =>
    for {
      todoList     <- TodoRepository.getAll()
      categoryList <- CategoryRepository.getAll()
    } yield {
      val viewValueTodo = todoList.map(i =>
        ViewValueTodo(
          i.id.toLong,
          i.v.title,
          i.v.body,
          i.v.state.name,
          categoryList
            .find(_.id.toLong == i.v.categoryId).map(_.v.name).getOrElse(""),
          categoryList
            .find(_.id.toLong == i.v.categoryId).map(_.v.color.name).getOrElse(
              ""
            )
        )
      )

      Ok(
        views.html.todo.list(
          ViewValueTodoList(
            "TODO List",
            Seq("main.css"),
            Seq("main.js"),
            viewValueTodo
          )
        )
      )
    }
  }

  def displayInsert() = Action.async { implicit req =>
    for {
      categoryList <- CategoryRepository.getAll()
    } yield {
      Ok(
        views.html.todo.insert(
          ViewValueTodoEdit(
            "TODO Add",
            Seq("main.css"),
            Seq("main.js"),
            categoryList
          ),
          todoInsertForm
        )
      )
    }
  }

  def insert() = Action.async { implicit req =>
    //Formバリデーションエラー判定
    todoInsertForm
      .bindFromRequest().fold(
        // エラー時遷移
        (formWithErrors: Form[TodoInsertFormData]) => {
          for {
            categoryList <- CategoryRepository.getAll()
          } yield {
            BadRequest(
              views.html.todo.insert(
                ViewValueTodoEdit(
                  "TODO Add",
                  Seq("main.css"),
                  Seq("main.js"),
                  categoryList
                ),
                todoInsertForm
              )
            )
          }
        },
        // 正常時遷移
        (f: TodoInsertFormData) => {
          for {
            result <- TodoRepository.add(
              Todo(f.categoryId, f.title, f.body, Todo.Status(0))
            )
          } yield {
            Redirect("/todos/list")
          }
        }
      )
  }

  def displayUpdate(id: Long) = Action.async { implicit req =>
    for {
      maybeUpdateTodo <- TodoRepository.get(Todo.Id(id))
      categoryList    <- CategoryRepository.getAll
    } yield maybeUpdateTodo match {
      case None             => BadRequest(views.html.error.error(defaultVv))
      case Some(updateTodo) =>
        val inputMap = Map(
          "id"         -> updateTodo.v.id.get.toString,
          "categoryId" -> updateTodo.v.categoryId.toString,
          "title"      -> updateTodo.v.title,
          "body"       -> updateTodo.v.body,
          "status"     -> updateTodo.v.state.code.toString
        )
        Ok(
          views.html.todo.update(
            ViewValueTodoEdit(
              "TODO Update",
              Seq("main.css"),
              Seq("main.js"),
              categoryList
            ),
            todoUpdateForm.bind(inputMap)
          )
        )
    }
  }

  def update() = Action.async { implicit req =>
    todoUpdateForm
      .bindFromRequest().fold(
        (formWithErrors: Form[TodoUpdateFormData]) => {
          for {
            categoryList <- CategoryRepository.getAll()
          } yield {
            BadRequest(
              views.html.todo.update(
                ViewValueTodoEdit(
                  "TODO Update",
                  Seq("main.css"),
                  Seq("main.js"),
                  categoryList
                ),
                formWithErrors
              )
            )
          }
        },
        (data: TodoUpdateFormData) => {

          val todo = Todo.build(
            Todo.Id(data.id),
            data.categoryId,
            data.title,
            data.body,
            Todo.Status(data.status)
          )

          for {
            result <- TodoRepository.update(todo)
          } yield result match {
            case Some(v) => Redirect("/todos/list")
            case _       => BadRequest(views.html.error.error(defaultVv))
          }
        }
      )
  }

  def delete(id: Long) = Action.async { implicit req =>
    for {
      result <- TodoRepository.remove(Todo.Id(id))
    } yield result match {
      case Some(v) => Redirect("/todos/list")
      case _       => BadRequest(views.html.error.error(defaultVv))
    }
  }
}
