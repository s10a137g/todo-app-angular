/**
  * to do sample project
  */

package controllers.todo

import javax.inject._
import play.api.mvc._

import persistence.persistence.onMySQL.{CategoryRepository, TodoRepository}

import model.ViewValueHome
import model.todo.Todo
import slick.jdbc.JdbcProfile
import ixias.persistence.SlickRepository
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

import scala.concurrent.duration._

import play.api.data._
import play.api.data.Forms._

import play.api.i18n.I18nSupport

// Todo追加フォーム
case class TodoInsertFormData(title: String, body: String, categoryId: Long)

//  Todo更新フォーム
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
    title  = "Todo一覧表示画面",
    cssSrc = Seq("main.css"),
    jsSrc  = Seq("main.js")
  )

  def list() = Action { implicit req =>
    val todoList     = Await.result(TodoRepository.getAll(), Duration.Inf)
    val categoryList = Await.result(CategoryRepository.getAll(), Duration.Inf)

    Ok(
      views.html.todo
        .list(defaultVv.copy(title = "TODO一覧表示画面"), todoList, categoryList)
    )
  }

  def displayInsert() = Action { implicit req =>
    val categoryList = Await.result(CategoryRepository.getAll(), Duration.Inf)

    Ok(
      views.html.todo.insert(
        defaultVv.copy(title = "TODO追加画面"),
        todoInsertForm,
        categoryList
      )
    )
  }

  def insert() = Action.async { implicit req =>
    //Formバリデーションエラー判定
    todoInsertForm
      .bindFromRequest().fold(
        // エラー時遷移
        (formWithErrors: Form[TodoInsertFormData]) => {
          val categoryList =
            Await.result(CategoryRepository.getAll(), Duration.Inf)

          Future{BadRequest(
            views.html.todo.insert(
              defaultVv.copy(title = "TODO追加画面"),
              formWithErrors,
              categoryList
            )
          )}
        },
        // 正常時遷移
        (f: TodoInsertFormData) => {
          for {
            result <- TodoRepository.add(Todo(f.categoryId, f.title, f.body, Todo.Status(0)))
          } yield result match {
            case v: Todo.Id => Redirect("/todos/list")
            case _       => BadRequest(views.html.error.error(defaultVv))
          }
        }
      )
  }

  def displayUpdate(id: Long) = Action { implicit req =>
    val updateTodo   =
      Await.result(TodoRepository.get(Todo.Id(id)), Duration.Inf).get
    val categoryList = Await.result(CategoryRepository.getAll(), Duration.Inf)
    val inputMap     = Map(
      "id"         -> updateTodo.v.id.get.toString,
      "categoryId" -> updateTodo.v.categoryId.toString,
      "title"      -> updateTodo.v.title,
      "body"       -> updateTodo.v.body,
      "status"     -> updateTodo.v.state.code.toString
    )

    Ok(
      views.html.todo.update(
        defaultVv.copy(title = "TODO更新画面"),
        todoUpdateForm.bind(inputMap),
        categoryList
      )
    )
  }

  def update() = Action.async { implicit req =>
    todoUpdateForm
      .bindFromRequest().fold(
        (formWithErrors: Form[TodoUpdateFormData]) => {
          val categoryList =
            Await.result(CategoryRepository.getAll(), Duration.Inf)

          Future {
            BadRequest(
              views.html.todo.update(
                defaultVv.copy(title = "TODO更新画面"),
                formWithErrors,
                categoryList
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
    val result = TodoRepository.remove(Todo.Id(id))

    Await.ready(result, Duration.Inf)
    Future{Redirect("/todos/list")}

  }

}
