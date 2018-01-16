package com.jobllers.service.jobscraper.impl.repo.eventSourcing.command

import akka.Done
import com.jobllers.service.jobscraper.impl.repo.models.ScrapedJob
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

sealed trait JobScraperCommand

case class StoreScrapedJobCommand(job: ScrapedJob) extends JobScraperCommand with ReplyType[Done]

object StoreScrapedJobCommand {
  implicit val format: Format[StoreScrapedJobCommand] = Json.format
}
