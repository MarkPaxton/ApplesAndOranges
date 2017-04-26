package services

import models.Product
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import repositories.ExampleProductRepository

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

      val checkout = new ExampleCheckoutService(mockProductRepo)

      checkout.parseCartData(validCartData) must be(scannedValidCartData)
    }

    "totalItems item names and return counts" in {
      val mockProductRepo = mock[ExampleProductRepository]

   //   when(mockProductRepo.getByName("orange")).thenReturn(Some(mock[Product]))
   //   when(mockProductRepo.getByName("apple")).thenReturn(Some(mock[Product]))

      val checkout = new ExampleCheckoutService(mockProductRepo)

      checkout.totalItems(scannedValidCartData) must be(expectedCartData)

    }

    "checkout counted known items and return total" in {
      val mockProductRepo = mock[ExampleProductRepository]
      val orange = Product("orange", BigDecimal("0.25"))
      val apple = Product("apple", BigDecimal("0.6"))

      when(mockProductRepo.getByName("orange")).thenReturn(Some(orange))
      when(mockProductRepo.getByName("apple")).thenReturn(Some(apple))

      val checkout = new ExampleCheckoutService(mockProductRepo)
      checkout.checkout(expectedCartData) must be(Right(BigDecimal("1.95")))
    }

    "checkout counted items with unknown item and return error" in {
      val mockProductRepo = mock[ExampleProductRepository]
      val orange = Product("orange", BigDecimal("0.25"))
      val apple = Product("apple", BigDecimal("0.6"))

      when(mockProductRepo.getByName("orange")).thenReturn(Some(orange))
      when(mockProductRepo.getByName("apple")).thenReturn(Some(apple))
      when(mockProductRepo.getByName("pear")).thenReturn(None)

      val checkout = new ExampleCheckoutService(mockProductRepo)
      checkout.checkout(unexpectedCartData) must be(Left("Invalid products: pear"))
    }
  }
}
