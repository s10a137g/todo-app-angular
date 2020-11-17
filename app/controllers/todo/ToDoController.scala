/**
 *
 * to do sample project
 *
 */

package controllers.todo

import javax.inject._
import play.api.mvc._

import persistence.persistence.onMySQL.TodoRepository

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

// TODO追加フォーム
case class TodoFormData(title: String, body: String, status: Int, category: Int)

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

  def displayInsert() = Action { implicit req =>
    val todoForm = Form(
      mapping(
          "title"  -> text, 
          "body"   -> text, 
          "status" -> number, 
          "category" -> number
        )(TodoFormData.apply)(TodoFormData.unapply)
      )
    val vv = ViewValueHome(
      title  = "Todo追加画面",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    Ok(views.html.todo.insert(vv, todoForm))
  }
    
  def insert() = Action { implicit req =>
    NoContent
  }
}
