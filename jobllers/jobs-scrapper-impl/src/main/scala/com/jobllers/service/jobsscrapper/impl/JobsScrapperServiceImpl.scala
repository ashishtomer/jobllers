package com.jobllers.service.jobsscrapper.impl

import akka.NotUsed
import akka.actor.ActorSystem
import com.jobllers.service.jobsscrapper.api.JobsScrapperServiceApi
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.SeedLinkExtractorActor
import com.jobllers.service.jobsscrapper.impl.scrapper.actors.SeedLinkExtractorActor.{FileName, SeedPageCSVDirectory}
import com.jobllers.service.jobsscrapper.impl.scrapper.naukri.NaukriJobScrapper
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.ResponseHeader
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

import scala.concurrent.Future

class JobsScrapperServiceImpl(actorSystem: ActorSystem) extends JobsScrapperServiceApi with NaukriJobScrapper {

  override def startScrapping(siteName: String): ServiceCall[NotUsed, String] = ServerServiceCall { (requestHeaders, nothing) ⇒
    val successHeaders = ResponseHeader.Ok.withHeaders(List(("Content-Type", "text/html")))
    actorSystem.actorOf(SeedLinkExtractorActor.props) ! SeedPageCSVDirectory(FileName(s"/tmp/jobllers/${siteName}SeedLinkDirectory.csv"))
    Future.successful(successHeaders, "\n\nActor has been assigned its job<br/>")
  }



}
