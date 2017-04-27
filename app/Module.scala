import com.google.inject.AbstractModule
import repositories._
import services._

class Module extends AbstractModule {
  def configure() = {

    bind(classOf[ProductRepository])
      .to(classOf[ExampleProductRepository])
      .asEagerSingleton()
    bind(classOf[OfferRepository])
      .to(classOf[ExampleOfferRepository])
      .asEagerSingleton()
    bind(classOf[CheckoutService])
      .to(classOf[ExampleCheckoutService])
      .asEagerSingleton()
  }
}