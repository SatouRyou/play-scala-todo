package controllers

import services.{Todo, TodoService}
import javax.inject.Inject

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import views._

/**
  * Created by teradashoutarou on 2016/10/12.
  */
class TodoController @Inject() (todoService: TodoService, val messagesApi: MessagesApi ) extends Controller with I18nSupport {

  def helloworld = Action { implicit request =>
    Ok("Hello World")
  }

  def create = Action {
    Ok(html.add(Forms.todoForm))
  }

  def save() = Action { implicit request =>
    val todoForm: TodoForm = Forms.todoForm.bindFromRequest().get
    todoService.insert(Todo(null,todoForm.todo.name))
    Redirect(routes.TodoController.list())
  }

  def list() = Action { implicit request =>
    val items: Seq[Todo] = todoService.list()
    Ok(html.list(items))
  }

  def getTodo(id: Long) = Action { implicit request =>
    val todo: Todo = todoService.findById(id)
    if(todo != null){
      Ok(html.update(Forms.todoForm.fill(TodoForm(None,todo))))
    } else {
      Redirect("/")
    }
  }

  def update(id: Long) = Action { implicit request =>
    Forms.todoForm.bindFromRequest.fold(
      errorForm => {
        BadRequest(html.update(errorForm))
      },
      successForm => {
        successForm.command match {
          case Some("update") => todoService.update(successForm.todo)
          case Some("delete") => todoService.delete(successForm.todo)
          case _ => Redirect("/todo/list")
        }
      }
    )
    Redirect("/todo/list")
  }
}

case class TodoForm( command: Option[String], todo: Todo )

object Forms {
  def todoForm = Form(
    mapping(
      "command" -> optional(text),
      "db" ->
        mapping(
          "id" -> optional(longNumber),
          "name" -> nonEmptyText
        )
        ((id, name) => new Todo(id, name))
        ((n: Todo) => Some((n.id, n.name)))
    )(TodoForm.apply)(TodoForm.unapply)
  )
}
