package com.jobllers.models

import com.jobllers.common.util.json.EnumJsonUtil
import play.api.libs.json.{Reads, Writes}

object SalaryTypeEnum extends Enumeration {
  type SalaryType = Value
  val salary, stipend = Value

  implicit def enumReads: Reads[SalaryType] = EnumJsonUtil.enumReads(SalaryTypeEnum)
  implicit def enumWrites: Writes[SalaryType] = EnumJsonUtil.enumWrites
}
