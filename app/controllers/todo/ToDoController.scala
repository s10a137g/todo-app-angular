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

@Singleton
class ToDoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def list() = Action { implicit req =>
    
    val vv = ViewValueHome(
      title  = "Todo一覧表示画面",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val todoList  = Await.result(TodoRepository.getAll(), Duration.Inf)
    Ok(views.html.todo.list(vv, todoList))
  }
}
