package com.jobllers.service.jobscraper.impl.repo.eventSourcing.entity

import akka.actor.ActorSystem
import com.jobllers.service.jobscraper.impl.repo.JobScraperRepository
import com.jobllers.service.jobscraper.impl.repo.eventSourcing.event.{JobScraperEvent, ScrapedJobStoredEvent}
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, ReadSideProcessor}

import scala.concurrent.ExecutionContext

class JobScraperProcessor(session: CassandraSession, readSide: CassandraReadSide, actorSystem: ActorSystem,
                          jobScraperRepository: JobScraperRepository)(implicit executionContext: ExecutionContext)
  extends ReadSideProcessor[JobScraperEvent] {

  override def buildHandler() =
    readSide.builder[JobScraperEvent]("offset")
      .setGlobalPrepare(jobScraperRepository.createTables)
      .setPrepare(_ ⇒ jobScraperRepository.createPreparedStmts())
      .setEventHandler[ScrapedJobStoredEvent](e ⇒ jobScraperRepository.insertScrapedJob(e.event.scrapedJob))
      .build()

  override def aggregateTags: Set[AggregateEventTag[JobScraperEvent]] = JobScraperEvent.Tag.allTags

}
