package com.jobllers.service.joblisting.impl

import com.jobllers.service.joblisting.api.JobListingService
import com.jobllers.service.joblisting.impl.elasticsearch.{ElasticSearch, ElasticSearchIndexedStore}
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

import scala.concurrent.ExecutionContext

abstract class JobListingApplication(context: LagomApplicationContext) extends LagomApplication(context) with AhcWSComponents {

  implicit val ec: ExecutionContext = executionContext

  lazy val indexedStore: ElasticSearchIndexedStore = wire[ElasticSearchIndexedStore]
  lazy val elasticSearch = serviceClient.implement[ElasticSearch]

  override lazy val lagomServer = serverFor[JobListingService](wire[JobListingServiceImpl])

}

class JobListingServiceLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext) = new JobListingApplication(context) {
    override def serviceLocator: ServiceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new JobListingApplication(context) with LagomDevModeComponents
}

