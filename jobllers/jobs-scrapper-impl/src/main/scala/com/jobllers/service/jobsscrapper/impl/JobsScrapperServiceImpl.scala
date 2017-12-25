package com.jobllers.service.jobsscrapper.impl

import akka.NotUsed
import com.jobllers.service.jobsscrapper.api.JobsScrapperServiceApi
import com.jobllers.service.jobsscrapper.impl.scrapper.NaukriDotComScrapper
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.ResponseHeader
import com.lightbend.lagom.scaladsl.server.ServerServiceCall

import scala.concurrent.Future

class JobsScrapperServiceImpl extends JobsScrapperServiceApi with NaukriDotComScrapper {

  override def getReport: ServiceCall[NotUsed, String] = ServerServiceCall { nothing ⇒
    Future.successful("Returning report")
  }

  override def startScrapping: ServiceCall[NotUsed, String] = ServerServiceCall { (requestHeaders, nothing) ⇒
    val successHeaders = ResponseHeader.Ok.withHeaders(List(("Content-Type", "text/html")))
    Future.successful(successHeaders, getJobsSeedLinks.mkString("<br/>"))
  }

}
