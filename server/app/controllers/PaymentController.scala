package controllers

import models.{AddPayment, Payment}

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.mutable

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class PaymentController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  private val paymentList = new mutable.ListBuffer[Payment]()

  def getPayments: Action[AnyContent] = Action { implicit request =>
    Ok(Json.toJson(paymentList))
  }

  def addPayment(): Action[AnyContent] = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val payment: Option[AddPayment] =
      jsonObject.flatMap(
        Json.fromJson[AddPayment](_).asOpt
      )

    payment match {
      case Some(newPayment) =>
        val addedPayment = Payment(newPayment.value)
        paymentList += addedPayment
        Created(Json.toJson(addedPayment))
      case None => BadRequest
    }
  }

}
