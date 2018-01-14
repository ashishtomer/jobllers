package com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.state

import com.jobllers.common.util.json.EnumJsonUtil
import play.api.libs.json.Format

object JobScrapperStatus extends Enumeration {
  type Status = Value
  val Stored, Deleted, Updated, StartingState = Value
  implicit val format: Format[Status] = EnumJsonUtil.enumFormat(JobScrapperStatus)
}
