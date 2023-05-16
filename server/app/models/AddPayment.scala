package models

import play.api.libs.json.Json

case class AddPayment(value: Long)

object AddPayment{
  implicit val addPaymentFormat = Json.format[AddPayment]
}
