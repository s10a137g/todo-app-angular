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
      title  = "List",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )

    val id: Todo.Id = Todo.Id(1L)
   
    val todoList = TodoRepository.getAll()
    
    val result = Await.result(todoList, Duration.Inf)
    println(result)

    Ok(views.html.todo.list(vv, result))
  }
}
