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
  
  def getTodo(id: Long) = Action.async { implicit req => 
    for { 
      maybeTodo         <- TodoRepository.get(Todo.Id(id))
      categoryList <- CategoryRepository.getAll()
    } yield maybeTodo match{
      case Some(todo) =>
        val viewValueTodo = 
          ViewValueTodo(
            todo.id.toLong,
            todo.v.title,
            todo.v.body,
            todo.v.state.name,
            categoryList
              .find(_.id.toLong == todo.v.categoryId).map(_.v.name).getOrElse(""),
            categoryList
              .find(_.id.toLong == todo.v.categoryId).map(_.v.color.name).getOrElse("")
          )
        
        implicit val todoWrites = Json.writes[ViewValueTodo]
        Ok(Json.toJson(viewValueTodo))
      
      case None => BadRequest("Bad")
    }
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
    val jsonInsertTodo = req.body.asJson.get.validate[JsonInsertTodo].get

    for {
      result <- TodoRepository.add(Todo(Category.Id(jsonInsertTodo.category.toLong), jsonInsertTodo.title, jsonInsertTodo.body, Todo.Status(0)))
    } yield{
      Ok(Json.toJson(req.toString))
    }

  }

  def updateTodos = Action.async { implicit req => 
    val jsonUpdateTodo = req.body.asJson.get.validate[JsonUpdateTodo].get

    val todo = Todo.build(
      Todo.Id(jsonUpdateTodo.id.toLong),
      Category.Id(jsonUpdateTodo.category.toLong),
      jsonUpdateTodo.title,
      jsonUpdateTodo.body,
      Todo.Status(jsonUpdateTodo.status.toShort)
    )

    for {
      result <- TodoRepository.update(todo)
    } yield{
      Ok(Json.toJson(req.toString))
    }

  }

  def deleteTodo(id: Long) = Action.async {
    for {
      result <- TodoRepository.remove(Todo.Id(id))
    } yield result match {
      case Some(v) => Ok(Json.toJson("OK"))
      case _       =>
        BadRequest(Json.toJson("NG"))
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

  def deleteCategory(id: Long) = Action.async { implicit req =>
    println(id)
    (for {
      categoryResult <- CategoryRepository.remove(Category.Id(id))
      todoResult     <- TodoRepository.removeByCategoryId(Category.Id(id))
    } yield {
      Ok(Json.toJson("OK"))
    }) recover {
      case e =>
        BadRequest(Json.toJson("NG"))
    }
  }
}

case class JsonInsertTodo(title: String, body: String, category: String)

object JsonInsertTodo {
    implicit val reads: Reads[JsonInsertTodo] = (
      (__ \ "title").read[String] and
      (__ \ "body").read[String] and
      (__ \ "category").read[String]
    ) (JsonInsertTodo.apply _)
  }

case class JsonUpdateTodo(id: String, title: String, body: String, status: String, category: String)

object JsonUpdateTodo {
    implicit val reads: Reads[JsonUpdateTodo] = (
      (__ \ "id").read[String] and
      (__ \ "title").read[String] and
      (__ \ "body").read[String] and
      (__ \ "status").read[String] and
      (__ \ "category").read[String]
    ) (JsonUpdateTodo.apply _)
  }
