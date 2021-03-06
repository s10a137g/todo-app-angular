package lib

package object persistence {

  val default = onMySQL

  object onMySQL {
    implicit lazy val driver = slick.jdbc.MySQLProfile
    object TodoRepository     extends TodoRepository
    object CategoryRepository extends CategoryRepository

  }
}
