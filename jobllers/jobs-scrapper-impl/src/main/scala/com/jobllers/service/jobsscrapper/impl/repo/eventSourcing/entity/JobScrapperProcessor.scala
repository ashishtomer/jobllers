package com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.entity

import akka.actor.ActorSystem
import com.jobllers.service.jobsscrapper.impl.repo.JobScrapperRepository
import com.jobllers.service.jobsscrapper.impl.repo.eventSourcing.event.{JobScrapperEvent, ScrappedJobStoredEvent}
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, ReadSideProcessor}

import scala.concurrent.ExecutionContext

class JobScrapperProcessor(session: CassandraSession, readSide: CassandraReadSide, actorSystem: ActorSystem,
                           jobScrapperRepository: JobScrapperRepository)(implicit executionContext: ExecutionContext)
  extends ReadSideProcessor[JobScrapperEvent] {

  override def buildHandler() =
    readSide.builder[JobScrapperEvent]("offset")
      .setGlobalPrepare(jobScrapperRepository.createTables)
      .setPrepare(_ ⇒ jobScrapperRepository.createPreparedStmts())
      .setEventHandler[ScrappedJobStoredEvent](e ⇒ jobScrapperRepository.insertScrappedJob(e.event.scrappedJob))
      .build()

  override def aggregateTags: Set[AggregateEventTag[JobScrapperEvent]] = JobScrapperEvent.Tag.allTags

}
