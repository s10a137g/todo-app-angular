package lib.persistence.db

import slick.jdbc.JdbcProfile

// Tableを扱うResourceのProvider
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
trait SlickResourceProvider[P <: JdbcProfile] {

  implicit val driver: P
  object CategoryTable extends CategoryTable
  object TodoTable extends TodoTable
  // --[ テーブル定義 ] --------------------------------------
  lazy val AllTables = Seq(
    CategoryTable,
    TodoTable
  )
}
