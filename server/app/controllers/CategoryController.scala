package controllers

import models.{AddCategory, Category}

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.mutable

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class CategoryController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  private val categoryList = new mutable.ListBuffer[Category]()
  categoryList+=Category(1,"cat 1")
  categoryList+=Category(2,"cat 2")
  categoryList+=Category(3,"cat 3")


  def getCategories: Action[AnyContent] = Action { implicit request =>
    Ok(Json.toJson(categoryList))
  }

  def getCategory(id: Long): Action[AnyContent] = Action { implicit request =>
    val category = categoryList.find(_.id==id)
    category match {
      case Some(c) => Ok(Json.toJson(c))
      case None => Redirect(routes.CategoryController.getCategories)
    }
  }

  def updateCategory(id: Long): Action[AnyContent] = Action { implicit request =>
    val category = categoryList.find(_.id == id)
    var index = -1
    category match {
      case Some(i) => index = categoryList.indexOf(i)
      case None =>
    }

    if (index != -1) {
      val content = request.body
      val jsonObject = content.asJson

      val proposedCategory: Option[AddCategory] =
        jsonObject.flatMap(
          Json.fromJson[AddCategory](_).asOpt
        )

      proposedCategory match {
        case Some(pr) =>
          val updatedCategory = Category(id, pr.name)
          categoryList.update(index,updatedCategory)
          Ok(Json.toJson(updatedCategory))
        case None => BadRequest
      }
    }
    else{
      BadRequest
    }
  }


  def deleteCategory(id: Long): Action[AnyContent] = Action {
    val kategoria = categoryList.find(_.id==id)
    var index= -1
    kategoria match {
      case Some(i) => index=categoryList.indexOf(i)
      case None =>
    }

    if(index != -1) {
      categoryList.remove(index)
    }

    Redirect("/categories")
  }


  def addCategory(): Action[AnyContent] = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val category: Option[AddCategory] =
      jsonObject.flatMap(
        Json.fromJson[AddCategory](_).asOpt
      )

    category match {
      case Some(newCategory) =>
        val nextId = categoryList.map(_.id).max + 1
        val addedCategory = Category(nextId, newCategory.name)
        categoryList += addedCategory
        Created(Json.toJson(addedCategory))
      case None => BadRequest
    }
  }

}
