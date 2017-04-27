package services

import models.{Offer, Product}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import repositories.{ExampleOfferRepository, ExampleProductRepository}

class ExampleCheckoutServiceSpec extends PlaySpec with GuiceOneAppPerTest with MockitoSugar {
  val validCartData = "orange,orange,apple,apple,orange"
  val invalidCartData = "pear,orange"

  val scannedValidCartData = Seq("orange","orange","apple","apple","orange")

  val expectedCartData = Map(
    "orange" -> 3,
    "apple" -> 2
  )

  val unexpectedCartData = Map(
    "orange" -> 3,
    "apple" -> 2,
    "pear" -> 7
  )
  "ExampleCheckoutService" should {

    "parse CartData and return item names" in {
      val mockProductRepo = mock[ExampleProductRepository]
      val mockOfferRepo = mock[ExampleOfferRepository]

      val checkout = new ExampleCheckoutService(mockProductRepo, mockOfferRepo)

      checkout.parseCartData(validCartData) must be(scannedValidCartData)
    }

    "totalItems item names and return counts" in {
      val mockProductRepo = mock[ExampleProductRepository]
      val mockOfferRepo = mock[ExampleOfferRepository]

      val checkout = new ExampleCheckoutService(mockProductRepo, mockOfferRepo)

      checkout.totalItems(scannedValidCartData) must be(expectedCartData)
    }

    "checkout counted known items and return total" in {
      val mockProductRepo = mock[ExampleProductRepository]
      val mockOfferRepo = mock[ExampleOfferRepository]

      val orange = Product("orange", BigDecimal("0.25"))
      val apple = Product("apple", BigDecimal("0.6"))

      when(mockProductRepo.getByName("orange")).thenReturn(Some(orange))
      when(mockProductRepo.getByName("apple")).thenReturn(Some(apple))

      val checkout = new ExampleCheckoutService(mockProductRepo, mockOfferRepo)
      checkout.checkout(expectedCartData) must be(Right(BigDecimal("1.95")))
    }

    "checkout counted known items with offers and return total" in {
      val mockProductRepo = mock[ExampleProductRepository]
      val mockOfferRepo = mock[ExampleOfferRepository]

      val orange = Product("orange", BigDecimal("0.25"))
      val apple = Product("apple", BigDecimal("0.6"))

      val appleOffer = Offer("apple", 1, 1)
      val orangeOffer = Offer("orange", 1, 1)

      when(mockProductRepo.getByName("orange")).thenReturn(Some(orange))
      when(mockProductRepo.getByName("apple")).thenReturn(Some(apple))

      when(mockOfferRepo.getByProduct("apple")).thenReturn(Some(appleOffer))
      when(mockOfferRepo.getByProduct("orange")).thenReturn(Some(orangeOffer))

      val checkout = new ExampleCheckoutService(mockProductRepo, mockOfferRepo)
      checkout.checkout(Map[String, Int]("apple" -> 2, "orange" -> 2)) must be(Right(BigDecimal("0.85")))
    }

    "checkout counted items with unknown item and return error" in {
      val mockProductRepo = mock[ExampleProductRepository]
      val mockOfferRepo = mock[ExampleOfferRepository]

      val orange = Product("orange", BigDecimal("0.25"))
      val apple = Product("apple", BigDecimal("0.6"))

      when(mockProductRepo.getByName("orange")).thenReturn(Some(orange))
      when(mockProductRepo.getByName("apple")).thenReturn(Some(apple))
      when(mockProductRepo.getByName("pear")).thenReturn(None)

      val checkout = new ExampleCheckoutService(mockProductRepo, mockOfferRepo)
      checkout.checkout(unexpectedCartData) must be(Left("Invalid products: pear"))
    }
  }
}
