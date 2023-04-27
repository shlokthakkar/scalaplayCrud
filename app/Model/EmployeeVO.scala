package Model

import slick.jdbc.MySQLProfile.api._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsObject, JsPath, Json, Reads, Writes}

case class EmployeeVO(employeeId: Option[Int],company: String,firstName: String,lastName: String,email: String,mobile: String,salary: Int)

class EmployeeTableDef(tag: Tag) extends Table[EmployeeVO](tag,"Employee"){

  def employeeId = column[Option[Int]]("employeeID",O.PrimaryKey,O.AutoInc)
  def company = column[String]("company")
  def firstName = column[String]("firstName")
  def lastName = column[String]("lastName")
  def email = column[String]("email")
  def mobile = column[String]("mobile")
  def salary = column[Int]("salary")

  override def * =
    (employeeId,company,firstName,lastName,email,mobile,salary) <>  ( (EmployeeVO.apply _).tupled,EmployeeVO.unapply )

}

object EmployeeVO{

  implicit val writes: Writes[EmployeeVO] = new Writes[EmployeeVO] {
    override def writes(o: EmployeeVO): JsObject = Json.obj(
      "employeeId" -> o.employeeId,
      "company" -> o.company,
      "firstName" -> o.firstName,
      "lastName" -> o.lastName,
      "email" -> o.email,
      "mobile"  -> o.mobile,
      "salary" -> o.salary
    )
  }

  implicit val reads: Reads[EmployeeVO] = (
    (JsPath \ "employeeId").readNullable[Int] and
      (JsPath \ "company").read[String] and
      (JsPath \ "firstName").read[String] and
      (JsPath \ "lastName").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "mobile").read[String] and
      (JsPath \ "salary").read[Int])(EmployeeVO.apply _)

}
