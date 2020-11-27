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

import model.todo.{ViewValueTodo, ViewValueTodoEdit, ViewValueTodoList}

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

@Singleton
class ApiController @Inject() (
  val controllerComponents: ControllerComponents
  ) extends BaseController
     with I18nSupport {

  def get = Action { implicit req => 
    val res: String = "レスポンスです！";

    Ok(Json.toJson(res));
  }
  
  def getTodos = Action.async { implicit req => 
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
      
      implicit val todoWrites = Json.writes[ViewValueTodo]
      
      Ok(Json.toJson(viewValueTodo))
      
    }
  }

  def insertTodos = Action.async { implicit req => 
    val jsonTodo = req.body.asJson.get.validate[JsonTodo].get

    for {
      result <- TodoRepository.add(Todo(Category.Id(jsonTodo.category.toLong), jsonTodo.title, jsonTodo.body, Todo.Status(0)))
    } yield{
      Ok(Json.toJson(req.toString))
    }

  }

  def getCategories = Action.async {implicit req =>
    for {
      categoryList <- CategoryRepository.getAll()
    } yield {
       
      val viewValueCategory = categoryList.map(i =>
          ViewValueCategory(i.id.toLong, i.v.name, i.v.slug, i.v.color.code, i.v.color.name)
      )
      
      implicit val categoryWrites = Json.writes[ViewValueCategory]

      Ok(Json.toJson(viewValueCategory))
    }
  }
}

case class JsonTodo(title: String, body: String, category: String)

object JsonTodo {
    implicit val reads: Reads[JsonTodo] = (
      (__ \ "title").read[String] and
      (__ \ "body").read[String] and
      (__ \ "category").read[String]
    ) (JsonTodo.apply _)
  }
