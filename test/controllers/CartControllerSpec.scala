package controllers

import akka.stream.Materializer
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


    "checkout with ok with valid items" in {
      implicit val materializer = app.injector.instanceOf(classOf[Materializer])

      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest().withTextBody("orange,orange,apple,apple,orange"))

      status(check) mustBe OK
      contentAsString(check) must be(("1.95"))
    }

    "checkout with bad request message invalid items" in {
      implicit val materializer = app.injector.instanceOf(classOf[Materializer])

      val controller = app.injector.instanceOf[CartController]
      val check = controller.post().apply(FakeRequest().withTextBody("orange,orange,pear,banana,apple,apple,apple"))

      status(check) mustBe BAD_REQUEST
      contentAsString(check) must be(("Invalid products: banana,pear"))
    }
  }
}
