package models

import play.api.libs.json.Json

case class Product(id: Long, name: String, price: Long)

object Product {
  implicit val productFormat = Json.format[Product]
}



