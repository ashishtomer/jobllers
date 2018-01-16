package com.jobllers.service.jobscraper.impl

import akka.NotUsed
import akka.actor.ActorSystem
import com.jobllers.service.jobscraper.api.JobScrapperServiceApi
import com.jobllers.service.jobscraper.impl.repo.eventSourcing.command.JobScraperCommand
import com.jobllers.service.jobscraper.impl.repo.eventSourcing.entity.JobScraperEntity
import com.jobllers.service.jobscraper.impl.scrapper.actors.SeedLinkExtractorActor
import com.jobllers.service.jobscraper.impl.scrapper.actors.SeedLinkExtractorActor.{FileName, SeedPageCSVDirectory}
import com.jobllers.service.jobscraper.impl.scrapper.naukri.NaukriJobScrapper
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.ResponseHeader
import com.lightbend.lagom.scaladsl.persistence.{PersistentEntityRef, PersistentEntityRegistry}
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

import scala.concurrent.Future

class JobScrapperServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, actorSystem: ActorSystem)
  extends JobScrapperServiceApi with NaukriJobScrapper {

  def ref(id: String): PersistentEntityRef[JobScraperCommand] = persistentEntityRegistry.refFor[JobScraperEntity](id)

  override def startScrapping(siteName: String): ServiceCall[NotUsed, String] = ServerServiceCall { (_, _) ⇒
    val successHeaders = ResponseHeader.Ok.withHeaders(List(("Content-Type", "text/html")))
    actorSystem.actorOf(SeedLinkExtractorActor.props) ! SeedPageCSVDirectory(FileName(s"/tmp/jobllers/${siteName}SeedLinkDirectory.csv"))
    Future.successful(successHeaders, "\n\nActor has been assigned its job<br/>")
  }


}
