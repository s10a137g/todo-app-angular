/**
 *
 * to do sample project
 *
 */

package controllers.todo

import javax.inject._
import play.api.mvc._

import persistence.persistence.onMySQL.{TodoRepository, CategoryRepository}

import model.ViewValueHome
import model.todo.Todo
import slick.jdbc.JdbcProfile
import ixias.persistence.SlickRepository
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await,Future}

import scala.concurrent.duration._

import play.api.data._
import play.api.data.Forms._

import play.api.i18n.I18nSupport

// Todo追加フォーム
case class TodoInsertFormData(title: String, body: String, category: String)

//  Todo更新フォーム
case class TodoUpdateFormData(id: String, title: String, body: String, status: String, category: String)

@Singleton
class TodoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport {
  val alphaNumAllowedLineBreak = """[A-Za-z1-9]"""
  val alphaNumDisallowedLineBreak = """[A-Za-z1-9]"""
  

  val todoInsertForm = Form(
    mapping(
      "title"  -> text, 
      "body"   -> text, 
      "category" -> text
    )(TodoInsertFormData.apply)(TodoInsertFormData.unapply)
  )

  val todoUpdateForm = Form(
    mapping(
      "id" -> text,
      "title"  -> text, 
      "body"   -> text, 
      "status" -> text,
      "category" -> text
    )(TodoUpdateFormData.apply)(TodoUpdateFormData.unapply)
  )

  def list() = Action { implicit req =>
    
    val vv = ViewValueHome(
      title  = "Todo一覧表示画面",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val todoList  = Await.result(TodoRepository.getAll(), Duration.Inf)
    Ok(views.html.todo.list(vv, todoList))
  }


  def displayInsert() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo追加画面",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    val categoryList  = Await.result(CategoryRepository.getAll(), Duration.Inf)
    Ok(views.html.todo.insert(vv, todoInsertForm, categoryList))
  }
    
  def insert() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo追加画面",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    
    //Formバリデーションエラー判定
    todoInsertForm.bindFromRequest().fold(
      // エラー時遷移
      (formWithErrors: Form[TodoInsertFormData]) => {
        val categoryList  = Await.result(CategoryRepository.getAll(), Duration.Inf)
        BadRequest(views.html.todo.insert(vv, formWithErrors, categoryList))
      },
      // 正常時遷移
      (f: TodoInsertFormData) => {
        var result = TodoRepository.add(Todo(f.category.toLong, f.title,f.body, Todo.Status(0)))
        Await.ready(result, Duration.Inf)
        Redirect("/todos/list")
      }
    )
  }
  
  def displayUpdate(id: Long) = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo更新画面",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    val todo = Await.result(TodoRepository.get(Todo.Id(id)), Duration.Inf).get
    val categoryList  = Await.result(CategoryRepository.getAll(), Duration.Inf)
    Ok(views.html.todo.update(vv, todoUpdateForm, categoryList, todo))
  }
    
  def update() = Action { implicit req =>

    todoUpdateForm.bindFromRequest().fold(

      (formWithErrors: Form[TodoUpdateFormData]) => {
        val vv = ViewValueHome(
          title  = "Todo更新画面",
          cssSrc = Seq("main.css"),
          jsSrc  = Seq("main.js")
        )

        val categoryList  = Await.result(CategoryRepository.getAll(), Duration.Inf)
        val todo = Await.result(TodoRepository.get(Todo.Id(1)), Duration.Inf).get

        BadRequest(views.html.todo.update(vv, formWithErrors, categoryList, todo))
      },

      (data: TodoUpdateFormData) => {
        println(data)
        val result = TodoRepository.update(
          Todo.build(
            Todo.Id(data.id.toLong),
            data.category.toLong,
            data.title,
            data.body,
            Todo.Status(data.status.toShort)
          )
        )
        Await.ready(result, Duration.Inf)

        val categoryList  = Await.result(CategoryRepository.getAll(), Duration.Inf)
        val todo = Await.result(TodoRepository.get(Todo.Id(1)), Duration.Inf).get
        Redirect("/todos/list")
        // result.value.get match {
        //   case Success(v) => Redirect("/todos/list")
        //   case Failure => BadRequest(views.html.todo.update(vv, formWithErrors, categoryList, todo))
        // }
      }) 
  }
}
