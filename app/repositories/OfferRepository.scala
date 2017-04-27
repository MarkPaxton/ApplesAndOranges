package repositories

import models.Offer


trait OfferRepository {
  def getByProduct(name: String): Option[Offer]
}


class ExampleOfferRepository extends OfferRepository {
  val offers = Map(
    "apple" -> Offer("apple", 1, 1), // buy 1 get one free
    "orange" -> Offer("orange", 2, 1) //buy 3 get one free
  )

  def getByProduct(name: String) = offers.get(name)
}