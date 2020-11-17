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
case class TodoUpdateFormData(title: String, body: String, status: Int, category: Int)

@Singleton
class ToDoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with I18nSupport {

  def list() = Action { implicit req =>
    
    val vv = ViewValueHome(
      title  = "Todo一覧表示画面",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val todoList  = Await.result(TodoRepository.getAll(), Duration.Inf)
    Ok(views.html.todo.list(vv, todoList))
  }

  val todoForm = Form(
    mapping(
      "title"  -> text, 
      "body"   -> text, 
      "category" -> text
    )(TodoInsertFormData.apply)(TodoInsertFormData.unapply)
  )

  def displayInsert() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo追加画面",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    val categoryList  = Await.result(CategoryRepository.getAll(), Duration.Inf)
    println(categoryList) 
    Ok(views.html.todo.insert(vv, todoForm, categoryList))
  }
    
  def insert() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "Todo追加画面",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    
    //Formバリデーションエラー判定
    todoForm.bindFromRequest().fold(
      // エラー時遷移
      (formWithErrors: Form[TodoInsertFormData]) => {
        val categoryList  = Await.result(CategoryRepository.getAll(), Duration.Inf)
        BadRequest(views.html.todo.insert(vv, formWithErrors, categoryList))
      },
      // 正常時遷移
      (f: TodoInsertFormData) => {
        TodoRepository.add(Todo(f.category.toLong, f.title,f.body, Todo.Status(0)))
        Redirect("/todos/list")
      }
    )
  }
  
}
