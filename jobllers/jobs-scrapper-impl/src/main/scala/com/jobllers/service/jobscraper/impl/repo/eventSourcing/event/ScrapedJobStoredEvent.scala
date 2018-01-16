package com.jobllers.service.jobscraper.impl.repo.eventSourcing.event

import com.jobllers.service.jobscraper.impl.repo.models.ScrapedJob
import play.api.libs.json.{Format, Json}

case class ScrapedJobStoredEvent(scrapedJob: ScrapedJob) extends JobScraperEvent

object ScrapedJobStoredEvent {
  implicit val format: Format[ScrapedJobStoredEvent] = Json.format
}
