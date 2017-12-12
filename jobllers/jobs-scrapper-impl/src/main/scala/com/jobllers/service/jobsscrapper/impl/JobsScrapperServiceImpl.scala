package com.jobllers.service.jobsscrapper.impl

import akka.NotUsed
import com.jobllers.service.jobsscrapper.api.JobsScrapperServiceApi
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

class JobsScrapperServiceImpl extends JobsScrapperServiceApi {

  override def getReport: ServiceCall[NotUsed, String] = ServiceCall { nothing ⇒
    Future.successful("Returning report")
  }

  override def startScrapping: ServiceCall[NotUsed, String] = ServiceCall { nothing ⇒
    Future.successful("Scrapping started")
  }

}
