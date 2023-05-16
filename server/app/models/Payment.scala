package models

import play.api.libs.json.Json

case class Payment(value: Long)

object Payment {
  implicit val paymentFormat = Json.format[Payment]
}
