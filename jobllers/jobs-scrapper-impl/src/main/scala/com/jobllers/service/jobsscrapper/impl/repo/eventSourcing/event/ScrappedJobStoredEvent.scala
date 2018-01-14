package com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.event

import com.jobllers.service.jobsscrapper.impl.repo.models.ScrapedJob
import play.api.libs.json.{Format, Json}

case class ScrappedJobStoredEvent(scrappedJob: ScrapedJob) extends JobScrapperEvent

object ScrappedJobStoredEvent {
  implicit val format: Format[ScrappedJobStoredEvent] = Json.format
}
