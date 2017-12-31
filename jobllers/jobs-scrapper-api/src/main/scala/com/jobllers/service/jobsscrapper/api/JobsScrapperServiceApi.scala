package com.jobllers.service.jobsscrapper.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.Service._
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait JobsScrapperServiceApi extends Service {

  def getReport: ServiceCall[NotUsed, String]
  def startScrapping(siteName: String): ServiceCall[NotUsed, String]

  override def descriptor: Descriptor =
    named("jobs_scrapper_service")
      .withCalls(
        restCall(Method.GET, "/report", getReport),
        restCall(Method.GET, "/startScrapping/:siteName", startScrapping _)
      )
      .withAutoAcl(true)

}
