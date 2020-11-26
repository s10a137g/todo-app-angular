package controllers.api

import javax.inject._
import play.api.mvc._

import lib.persistence.onMySQL.{CategoryRepository, TodoRepository}
import lib.model.Category
import model.ViewValueHome
import model.error.ViewValueError
import model.category.{
  ViewValueCategory,
  ViewValueCategoryEdit,
  ViewValueCategoryList
}
import lib.model.Todo
import slick.jdbc.JdbcProfile
import ixias.persistence.SlickRepository
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.concurrent.duration._

import play.api.data._
import play.api.data.Forms._

import play.api.i18n.I18nSupport
import play.api.data.validation.Constraint
import play.api.data.validation
import play.api.data.validation.{Invalid, Valid, ValidationError}

import play.api.libs.json.{JsNull,Json,JsString,JsValue}

@Singleton
class ApiController @Inject() (
  val controllerComponents: ControllerComponents
  ) extends BaseController
     with I18nSupport {

  def get = Action { implicit req => 
    val res: String = "レスポンスです！";

    Ok(Json.toJson(res));
  }
}
