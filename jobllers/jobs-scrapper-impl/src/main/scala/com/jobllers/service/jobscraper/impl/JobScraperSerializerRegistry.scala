package com.jobllers.service.jobscraper.impl

import com.jobllers.service.jobscraper.impl.repo.eventSourcing.command.StoreScrapedJobCommand
import com.jobllers.service.jobscraper.impl.repo.eventSourcing.event.ScrapedJobStoredEvent
import com.jobllers.service.jobscraper.impl.repo.eventSourcing.state.JobScraperState
import com.jobllers.service.jobscraper.impl.repo.models.ScrapedJob
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

import scala.collection.immutable

object JobScraperSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: immutable.Seq[JsonSerializer[_]] = {
    immutable.Seq(
      JsonSerializer[StoreScrapedJobCommand],
      JsonSerializer[ScrapedJobStoredEvent],
      JsonSerializer[JobScraperState],
      JsonSerializer[ScrapedJob]
    )
  }
}
