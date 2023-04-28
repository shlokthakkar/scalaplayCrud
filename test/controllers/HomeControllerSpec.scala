package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "HomeController GET" should {

    "get all employees in TGT" in {
      val controller = app.injector.instanceOf(classOf[HomeController])
//      val dbc =  DatabaseConfigProvider
//      val dao = new EmployeeDAO(dbc)
//      val service = new EmployeeService(dao)
//      val controller = new HomeController(Helpers.stubControllerComponents(),service)
      val req = controller.getEmployee("TGT").apply(FakeRequest(GET, "/Employee/:company"))

      status(req) mustBe OK
      contentType(req) mustBe Some("application/json")
    }

    "not found Exception for get all Employees for Samsung" in {
      val controller = app.injector.instanceOf(classOf[HomeController])
      //      val dbc =  DatabaseConfigProvider
      //      val dao = new EmployeeDAO(dbc)
      //      val service = new EmployeeService(dao)
      //      val controller = new HomeController(Helpers.stubControllerComponents(),service)
      val home = controller.getEmployee("Samsung").apply(FakeRequest(GET, "/Employee/:company"))

      status(home) mustBe NOT_FOUND
      contentType(home) mustBe Some("text/plain")
    }
  }
}
