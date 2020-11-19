/**
  * to do sample project
  */

package controllers.category

import javax.inject._
import play.api.mvc._

import persistence.persistence.onMySQL.{CategoryRepository, TodoRepository}
import model.category.Category
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
import play.api.data.validation.Constraint
import play.api.data.validation
import play.api.data.validation.{Invalid, Valid, ValidationError}

// Category追加フォーム
case class CategoryInsertFormData(name: String, slug: String, color: Short)

//  Category更新フォーム
case class CategoryUpdateFormData(
  id:    Long,
  name:  String,
  slug:  String,
  color: Short
)

@Singleton
class CategoryController @Inject() (
  val controllerComponents: ControllerComponents
) extends BaseController
     with I18nSupport {

  val numericAlphaChar = """[A-Za-z0-9]""".r

  val slugCheckConstraint: Constraint[String] =
    Constraint("constraints.slugcheck")({ plainText =>
      val errors = plainText match {
        case numericAlphaChar() => Nil
        case _                  => Seq(ValidationError("スラッグは英数字のみ入力可能です。"))
      }

      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
    })

  val slugCheck: Mapping[String] = nonEmptyText.verifying(slugCheckConstraint)

  val categoryInsertForm = Form(
    mapping(
      "name"  -> nonEmptyText,
      "slug"  -> slugCheck,
      "color" -> shortNumber
    )(CategoryInsertFormData.apply)(CategoryInsertFormData.unapply)
  )

  val categoryUpdateForm = Form(
    mapping(
      "id"    -> longNumber,
      "name"  -> nonEmptyText,
      "slug"  -> slugCheck,
      "color" -> shortNumber
    )(CategoryUpdateFormData.apply)(CategoryUpdateFormData.unapply)
  )

  val defaultVv = ViewValueHome(
    title  = "カテゴリ一覧表示画面",
    cssSrc = Seq("main.css"),
    jsSrc  = Seq("main.js")
  )

  def list = Action.async { implicit req =>
    for {
      categoryList <- CategoryRepository.getAll()
    } yield categoryList match {
      case v: Seq[CategoryRepository.EntityEmbeddedId] =>
        Ok(
          views.html.category
            .list(defaultVv.copy(title = "カテゴリ一覧表示画面"), categoryList)
        )
      case _                        => BadRequest(views.html.error.error(defaultVv))
    }
  }

  def displayInsert = Action { implicit req =>
    val categoryList = Await.result(CategoryRepository.getAll(), Duration.Inf)

    Ok(
      views.html.category
        .insert(defaultVv.copy(title = "カテゴリ追加画面"), categoryInsertForm)
    )
  }

  def insert = Action.async { implicit req =>
    //Formバリデーションエラー判定
    categoryInsertForm
      .bindFromRequest().fold(
        // エラー時遷移
        (formWithErrors: Form[CategoryInsertFormData]) => {

          Future {
            BadRequest(
              views.html.category
                .insert(defaultVv.copy(title = "カテゴリ追加画面"), formWithErrors)
            )
          }
        },
        // 正常時遷移
        (f: CategoryInsertFormData) => {
          for {
            result <- CategoryRepository.add(
              Category(f.name, f.slug, Category.Color(f.color))
            )
          } yield result match {
            case v: Category.Id => Redirect("/categories/list")
            case _              => BadRequest(views.html.error.error(defaultVv))
          }
        }
      )
  }
}
