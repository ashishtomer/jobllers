package com.jobllers.service.jobscraper.impl.repo.eventSourcing.state

import com.jobllers.common.util.json.EnumJsonUtil
import play.api.libs.json.Format

object JobScraperStatus extends Enumeration {
  type Status = Value
  val Stored, Deleted, Updated, StartingState = Value
  implicit val format: Format[Status] = EnumJsonUtil.enumFormat(JobScraperStatus)
}
