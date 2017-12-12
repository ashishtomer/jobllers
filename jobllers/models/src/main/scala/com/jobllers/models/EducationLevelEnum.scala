package com.jobllers.models

import com.jobllers.common.util.json.EnumJsonUtil
import play.api.libs.json.{Reads, Writes}

object EducationLevelEnum extends Enumeration {
  type EducationLevel = Value
  val highSchool = Value("High School")
  val diploma = Value("Diploma")
  val graduation = Value("Graduation")
  val phd = Value("PhD")
  val postGraduation = Value("Post Graduation")
  val mPhil = Value("MPhil")

  implicit val enumReads: Reads[EducationLevel] = EnumJsonUtil.enumReads(EducationLevelEnum)
  implicit def enumWrites: Writes[EducationLevel] = EnumJsonUtil.enumWrites
}
