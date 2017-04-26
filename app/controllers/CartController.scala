package controllers

import javax.inject._

import play.api.mvc._
import services.CheckoutService

class CartController @Inject()(checkoutService: CheckoutService) extends Controller {

  def post = Action { implicit request =>
    request.body.asText match {
      case Some(bodyText) => {
        val items = checkoutService.parseCartData(bodyText)
        val products = checkoutService.totalItems(items)
        checkoutService.checkout(products) match {
          case Left(m) => BadRequest(m)
          case Right(t) => Ok(t.toString)
        }
      }
      case _ => BadRequest
    }
  }

}
