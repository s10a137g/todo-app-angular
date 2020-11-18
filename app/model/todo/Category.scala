package model.todo

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

// カテゴリーを表すモデル
//~~~~~~~~~~~~~~~~~~~~
import Category._
case class Category(
  id:        Option[Id],
  name:      String,
  slug:      String,
  color:     Short,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object Category {

  val Id = the[Identity[Id]]
  type Id         = Long @@ Category
  type WithNoId   = Entity.WithNoId[Id, Category]
  type EmbeddedId = Entity.EmbeddedId[Id, Category]

  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(
    categoryId: Long,
    name:       String,
    slug:       String,
    color:      Short
  ): WithNoId = {
    new Entity.WithNoId(
      new Category(
        id    = None,
        name  = name,
        slug  = slug,
        color = color
      )
    )
  }
}
