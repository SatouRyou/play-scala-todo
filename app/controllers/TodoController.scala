package controllers


import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import services.Todo
import views._

/**
  * Created by teradashoutarou on 2016/10/12.
  */
class TodoController @Inject() ( val messagesApi: MessagesApi ) extends Controller with I18nSupport {

  def helloworld = Action { implicit request =>
    Ok("Hello World")
  }

  def list() = Action { implicit request =>
    val items: Seq[Todo] = Seq(Todo(null,"Todo1"), Todo(null,"Todo2"))
    Ok(html.list(items))
  }
}
