package services

import javax.inject.Inject
import anorm.SqlParser._
import anorm._
import play.api.db.DBApi
import scala.language.postfixOps

/**
  * Created by teradashoutarou on 2016/10/12.
  */
@javax.inject.Singleton
class TodoService @Inject() (dbapi: DBApi) {

  private val db = dbapi.database("default")

  val simple = {
    get[Option[Long]]("todo.id") ~
      get[String]("todo.name") map {
      case id ~ name => Todo(id,name)
    }
  }

  def list(): Seq[Todo] = {
    db.withConnection { implicit connection =>
      SQL(
        """
          select * from todo
        """
      ).as(simple *)
    }
  }
}
