package controllers

import Model.EmployeeVO
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.Format.GenericFormat

import javax.inject._
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import services.EmployeeService

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,val employeeService: EmployeeService) extends BaseController {


  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  //Get all Employees
  def getEmployee(company:String): Action[AnyContent] = Action async { implicit request: Request[AnyContent] =>
    employeeService.getEmployee(company).map {
      result => Ok(Json.toJson(result))
    }

  }

  //Get Employee by ID
  def getEmployeeById(company:String,id :Int): Action[AnyContent] = Action async { implicit request: Request[AnyContent] =>
    employeeService.getEmployeeById(company,id).map {
      result => if(result.isEmpty)
        {
          NotFound("Data Not Found")
        }
      else
        {
          Ok(Json.toJson(result))
        }
    }

  }

  //Add Employee
  def addEmployee(): Action[JsValue] = Action(parse.json) async { implicit request =>

    request.body.validate[EmployeeVO].map{

      employee => employeeService.addEmployee(employee).map{
        result => Ok(Json.toJson(result))
      }
    }.recoverTotal(
      e => Future{Ok("Bad Request"+ e)}
      )

  }



  //Delete Employee
  def deleteEmployee(id:Int): Action[AnyContent] = Action async { implicit request:Request[AnyContent] =>

    employeeService.deleteEmployee(id).map{
      result => if(result==1)
        {
          Ok(s"Deleted id $id")
        }
      else
        {
          Ok(s"ID : $id not found")
        }
    }

  }


  //Update the data of Employee
  def updateEmployee(company:String,id:Int): Action[JsValue] = Action(parse.json) async { implicit request =>

    val employee = request.body.as(EmployeeVO.reads)
    val resultObj = employeeService.update(company,id,employee)
    val result =Await.result(resultObj,Duration.Inf)
    Future{Ok(Json.toJson(result))}
  }

}
