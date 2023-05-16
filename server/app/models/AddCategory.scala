package models

import play.api.libs.json.Json

case class AddCategory(name: String)

object AddCategory{
  implicit val addCategoryFormat = Json.format[AddCategory]
}
