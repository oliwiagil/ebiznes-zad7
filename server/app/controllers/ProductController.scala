package controllers

import models.{AddProduct, Product}

import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.mutable

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class ProductController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */

  private val productList = new mutable.ListBuffer[Product]()
  productList+=Product(1,"first", 15)
  productList+=Product(2,"second", 25)
  productList+=Product(3,"third", 50)
  
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def getProducts: Action[AnyContent] = Action { implicit request =>
    Ok(Json.toJson(productList))
  }

  def getProduct(id: Long): Action[AnyContent] = Action { implicit request =>
    val produkt = productList.find(_.id==id)
    produkt match {
      case Some(p) => Ok(Json.toJson(p))
      case None => Redirect(routes.ProductController.getProducts)
    }
  }


  def updateProduct(id: Long): Action[AnyContent] = Action { implicit request =>
    val produkt = productList.find(_.id == id)
    var index = -1
    produkt match {
      case Some(i) => index = productList.indexOf(i)
      case None =>
    }

    if (index != -1) {
     val content = request.body
     val jsonObject = content.asJson

     val proposedProduct: Option[AddProduct] =
       jsonObject.flatMap(
         Json.fromJson[AddProduct](_).asOpt
       )

     proposedProduct match {
       case Some(pr) =>
         val updatedProduct = Product(id, pr.name, pr.price)
         productList.update(index,updatedProduct)
         Ok(Json.toJson(updatedProduct))
       case None => BadRequest
     }
    }
    else{
      BadRequest
    }

  }


  def deleteProduct(id: Long): Action[AnyContent] = Action {
    val produkt = productList.find(_.id==id)
    var index= -1
    produkt match {
      case Some(i) => index=productList.indexOf(i)
      case None =>
    }

    if(index != -1) {
      productList.remove(index)
    }

    Redirect("/products")
  }


  def addProduct(): Action[AnyContent] = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson

    val product: Option[AddProduct] =
      jsonObject.flatMap(
        Json.fromJson[AddProduct](_).asOpt
      )

    product match {
      case Some(newProduct) =>
        val nextId = productList.map(_.id).max + 1
        val addedProduct = Product(nextId, newProduct.name, newProduct.price)
        productList += addedProduct
        Created(Json.toJson(addedProduct))
      case None => BadRequest
    }
  }

}
