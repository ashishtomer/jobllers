package com.jobllers.service.jobscraper.impl

import com.jobllers.service.jobscraper.api.JobScrapperServiceApi
import com.jobllers.service.jobscraper.impl.repo.JobScraperRepository
import com.jobllers.service.jobscraper.impl.repo.eventSourcing.entity.{JobScraperEntity, JobScraperProcessor}
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

abstract class JobScrapperApplication(context: LagomApplicationContext) extends LagomApplication(context)
  with AhcWSComponents with CassandraPersistenceComponents {

  override lazy val lagomServer: LagomServer = serverFor[JobScrapperServiceApi](wire[JobScrapperServiceImpl])
  override lazy val jsonSerializerRegistry: JobScraperSerializerRegistry.type = JobScraperSerializerRegistry
  lazy implicit val transactionRepository: JobScraperRepository = wire[JobScraperRepository]
  readSide.register(wire[JobScraperProcessor])
  persistentEntityRegistry.register(wire[JobScraperEntity])

}

class JobsScrapperServiceLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext) = new JobScrapperApplication(context) {
    override def serviceLocator: ServiceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new JobScrapperApplication(context) with LagomDevModeComponents

}
