package com.jobllers.common.models.job

import com.jobllers.common.util.json.EnumJsonUtil
import play.api.libs.json.{Reads, Writes}

object VisaTypeEnum extends Enumeration {
  type VisaType = Value
  val residential, work, migration = Value

  implicit def enumReads: Reads[VisaType] = EnumJsonUtil.enumReads(VisaTypeEnum)
  implicit def enumWrites: Writes[VisaType] = EnumJsonUtil.enumWrites
}
