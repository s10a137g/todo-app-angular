/**
  * This is a sample of Todo Application.
  */

package model.todo

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

// ユーザーを表すモデル
//~~~~~~~~~~~~~~~~~~~~
import Todo._
case class Todo(
  id:         Option[Id],
  categoryId: Long,
  title:      String,
  body:       String,
  state:      Status,
  updatedAt:  LocalDateTime = NOW,
  createdAt:  LocalDateTime = NOW
) extends EntityModel[Id]

// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object Todo {

  val Id = the[Identity[Id]]
  type Id         = Long @@ Todo
  type WithNoId   = Entity.WithNoId[Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]

  // ステータス定義
  //~~~~~~~~~~~~~~~~~
  sealed abstract class Status(val code: Short, val name: String)
    extends EnumStatus
  object Status extends EnumStatus.Of[Status] {
    case object TODO     extends Status(code = 0, name = "TODO(着手前)")
    case object PROGRESS extends Status(code = 1, name = "進行中")
    case object COMPLETE extends Status(code = 2, name = "完了")
  }

  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(
    categoryId: Long,
    title:      String,
    body:       String,
    state:      Status
  ): WithNoId = {
    new Entity.WithNoId(
      new Todo(
        id         = None,
        categoryId = categoryId,
        title      = title,
        body       = body,
        state      = state
      )
    )
  }

  // EmbeddedIdインスタンスを生成するメソッド
  def build(
    id:         Id,
    categoryId: Long,
    title:      String,
    body:       String,
    state:      Status
  ): EmbeddedId = {
    new Entity.EmbeddedId(
      new Todo(
        id         = Some(id),
        categoryId = categoryId,
        title      = title,
        body       = body,
        state      = state
      )
    )
  }
}
