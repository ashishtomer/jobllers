package com.jobllers.models

import com.jobllers.common.util.json.EnumJsonUtil
import play.api.libs.json.{Reads, Writes}

object JobTypeEnum extends Enumeration {
  type JobType = Value
  val fullTime = Value("Full Time")
  val partTime = Value("Part Time")
  val contract = Value("Contract")
  val internship = Value("Internship")
  val remote = Value("Remote")

  implicit val enumReads: Reads[JobType] = EnumJsonUtil.enumReads(JobTypeEnum)
  implicit val enumWrites: Writes[JobType] = EnumJsonUtil.enumWrites
}
