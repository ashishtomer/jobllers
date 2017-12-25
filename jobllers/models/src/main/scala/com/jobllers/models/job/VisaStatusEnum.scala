package com.jobllers.models.job

import com.jobllers.common.util.json.EnumJsonUtil
import play.api.libs.json.{Reads, Writes}

object VisaStatusEnum extends Enumeration {
  type VisaStatus = Value
  val approved = Value("Approved")
  val reApplied = Value("Re-Applied")
  val declined = Value("Declined")
  val inProcess = Value("Under Process")

  implicit def enumReads: Reads[VisaStatus] = EnumJsonUtil.enumReads(VisaStatusEnum)
  implicit def enumWrites: Writes[VisaStatus] = EnumJsonUtil.enumWrites
}
