package com.jobllers.models

import com.jobllers.common.util.json.EnumJsonUtil
import play.api.libs.json.{Reads, Writes}

object SalaryCycleEnum extends Enumeration {
  type SalaryCycle = Value
  val yearly, monthly, quarterly = Value

  implicit def enumReads: Reads[SalaryCycle] = EnumJsonUtil.enumReads(SalaryCycleEnum)
  implicit def enumWrites: Writes[SalaryCycle] = EnumJsonUtil.enumWrites
}
