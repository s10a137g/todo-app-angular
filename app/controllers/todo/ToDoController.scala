/**
 *
 * to do sample project
 *
 */

package controllers.todo

import javax.inject._
import play.api.mvc._

import model.ViewValueHome

@Singleton
class ToDoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def list() = Action { implicit req =>
    val vv = ViewValueHome(
      title  = "List",
      cssSrc = Seq("main.css"),
      jsSrc  = Seq("main.js")
    )
    Ok(views.html.Home(vv))
  }
}
