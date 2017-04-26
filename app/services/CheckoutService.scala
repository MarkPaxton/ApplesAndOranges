package services

import javax.inject.Inject

import repositories.ProductRepository

trait CheckoutService {
  def parseCartData(data:String):Seq[String]

  def totalItems(items:Seq[String]): Map[String, Int]

  def checkout(items:Map[String, Int]):Either[String, BigDecimal]
}


class ExampleCheckoutService @Inject() (products:ProductRepository) extends CheckoutService{
  override def parseCartData(data: String): Seq[String] = {
    data.toLowerCase.split(",")
  }

  override def totalItems(items: Seq[String]): Map[String, Int] = {
    items.foldLeft(Map[String, Int]())((total, item) => total.updated(item, total.get(item).fold(1)(_+1)))
  }

  override def checkout(items: Map[String, Int]): Either[String, BigDecimal] = {
    val productItems = items.map {
      case (name, amount) => (name, products.getByName(name), amount)
    } toSeq

    val invalidProducts = productItems.filter(_._2.isEmpty)
    if(!invalidProducts.isEmpty) {
      Left(s"Invalid products: ${invalidProducts.map(_._1).sorted.mkString(",")}")
    } else {
      Right(productItems.map(productItem => productItem._2.get.price * productItem._3).sum)
    }
  }
}
