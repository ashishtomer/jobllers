package com.jobllers.service.jobscraper.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait JobScrapperServiceApi extends Service {

  def startScrapping(siteName: String): ServiceCall[NotUsed, String]

  override def descriptor: Descriptor =
    named("jobs_scrapper_service")
      .withCalls(
        restCall(Method.GET, "/startScrapping/:siteName", startScrapping _)
      )
      .withAutoAcl(true)

}
