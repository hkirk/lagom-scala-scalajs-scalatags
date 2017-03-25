package controllers

import javax.inject.Inject

import play.api.http._
import play.api.mvc._
import shared.SharedMessages
import views.{IndexView, MainView}

import scalatags._

class Application @Inject()(implicit env: play.Environment)
    extends Controller {

  def index: Action[AnyContent] = ok(IndexView(SharedMessages.itWorks))

  private def ok(view: Seq[Text.TypedTag[String]]) = Action {
    implicit val codec = Codec.utf_8
    Ok(MainView(view).toString).as(ContentTypes.HTML)
  }

}
