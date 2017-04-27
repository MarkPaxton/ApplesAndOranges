package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test._
import play.api.test.Helpers._

class CartControllerSpec extends PlaySpec with GuiceOneAppPerTest {

  "CartController POST" should {

    "return error with no body" in {
      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest())
      status(check) must be(BAD_REQUEST)
    }

    "checkout with ok with valid items with orange" in {

      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest().withTextBody("orange"))

      status(check) mustBe OK
      contentAsString(check) must be(("0.25"))
    }

    "checkout with ok with valid items with apple" in {

      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest().withTextBody("apple"))

      status(check) mustBe OK
      contentAsString(check) must be(("0.60"))
    }

    "checkout with ok with valid items with no offers" in {

      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest().withTextBody("orange,orange,apple"))

      status(check) mustBe OK
      contentAsString(check) must be(("1.10"))
    }

    "checkout with ok with valid items with orange offer" in {

      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest().withTextBody("orange,orange,orange"))

      status(check) mustBe OK
      contentAsString(check) must be(("0.50"))
    }

    "checkout with ok with valid items with apple offer" in {

      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest().withTextBody("apple,apple"))

      status(check) mustBe OK
      contentAsString(check) must be(("0.60"))
    }

    "checkout with ok with valid items with offers" in {

      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest().withTextBody("orange,orange,apple,apple,orange"))

      status(check) mustBe OK
      contentAsString(check) must be(("1.10"))
    }

    "checkout with bad request message invalid items" in {

      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest().withTextBody("orange,orange,pear,banana,apple,apple,apple"))

      status(check) mustBe BAD_REQUEST
      contentAsString(check) must be(("Invalid products: banana,pear"))
    }
  }
}
