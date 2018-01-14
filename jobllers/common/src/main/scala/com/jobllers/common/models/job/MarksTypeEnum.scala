package com.jobllers.common.models.job

import com.jobllers.common.util.json.EnumJsonUtil
import play.api.libs.json.{Reads, Writes}

object MarksTypeEnum extends Enumeration {
  type MarksType = Value
  val gpa = Value("GPA")
  val percentage = Value("%")

  implicit def enumReads: Reads[MarksType] = EnumJsonUtil.enumReads(MarksTypeEnum)
  implicit def enumWrites: Writes[MarksType] = EnumJsonUtil.enumWrites
}
