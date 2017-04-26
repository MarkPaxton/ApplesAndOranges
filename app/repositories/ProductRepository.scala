package repositories

import models.Product


trait ProductRepository {
  def getByName(name:String): Option[Product]
}


class ExampleProductRepository extends ProductRepository {
  val products = Map(
      "apple" -> Product("apple", BigDecimal("0.60")),
      "orange" -> Product("orange", BigDecimal("0.25"))
  )

  def getByName(name:String) = products.get(name)

}