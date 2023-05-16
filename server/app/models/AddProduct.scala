package models

import play.api.libs.json.Json

case class AddProduct(name: String, price: Long)

object AddProduct {
  implicit val addProductFormat = Json.format[AddProduct]
}
