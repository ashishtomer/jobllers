package com.jobllers.common.models.job

import SalaryCycleEnum.SalaryCycle
import SalaryTypeEnum.SalaryType
import play.api.libs.json.{Format, Json}

case class Salary(
                   salaryType: SalaryType,
                   amount: Double,
                   currency: String, //TODO: Create the enums for this, or use JodaMoney
                   salaryCycle: SalaryCycle
                 )

object Salary {
  implicit val format: Format[Salary] = Json.format
}
