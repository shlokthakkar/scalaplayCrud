package controllers

import Model.EmployeeVO
import play.api.libs.json.Format.GenericFormat

import javax.inject._
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import play.api.mvc._
import services.EmployeeService

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

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
  // Path is GET /Employee/companyName
  def getEmployee(company:String): Action[AnyContent] = Action async { implicit request: Request[AnyContent] =>
    employeeService.getEmployee(company).map {
      result => if(result.isEmpty)
        {
          NotFound("DATA_NOT_FOUND")
        }
      else {
          Ok(Json.toJson(result))
        }
    }

  }

  //Get Employee by ID
  // Path is GET /Employee/companyName/id
  def getEmployeeById(company:String,id :Int): Action[AnyContent] = Action async { implicit request: Request[AnyContent] =>
    employeeService.getEmployeeById(company,id).map {
      result => if(result.isEmpty)
        {
          NotFound("DATA_NOT_FOUND")
        }
      else
        {
          Ok(Json.toJson(result))
        }
    }

  }

  //Add Employee
  // Path is POST /Employee
  // add json format of employee to be added in postman
  def addEmployee(): Action[JsValue] = Action(parse.json) async { implicit request =>

    request.body.validate[EmployeeVO].map{

      employee => employeeService.addEmployee(employee).map{
        result => Ok(Json.toJson(result))
      }
    }.recoverTotal(
      e => Future{NotAcceptable("BAD_REQUEST")}
      )

  }



  //Delete Employee
  // Path is DELETE /Employee/id
  def deleteEmployee(id:Int): Action[AnyContent] = Action async { implicit request:Request[AnyContent] =>

    employeeService.deleteEmployee(id).map{
      case 1=> Ok("DELETED")
      case _=> NotFound("DATA_NOT_FOUND")
    }

  }


  //Update the data of Employee
  // Path is PUT /Employee/companyName/id
  def updateEmployee(company:String,id:Int): Action[JsValue] = Action(parse.json) async { implicit request =>

    val employee = request.body.as(EmployeeVO.reads)
    val resultObj = employeeService.update(company,id,employee)
    val result = Await.result(resultObj,Duration.Inf)
    if(result!=null)
      {
        Future{Ok(Json.toJson(result))}
      }
      else {
      Future{NotFound("DATA_NOT_FOUND")}
    }
  }

}
