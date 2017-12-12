package com.jobllers.service.jobsscrapper.impl

import com.jobllers.service.jobsscrapper.api.JobsScrapperServiceApi
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader, LagomServer}
import com.softwaremill.macwire.wire
import play.api.libs.ws.ahc.AhcWSComponents

abstract class JobsScrapperApplication(context: LagomApplicationContext) extends LagomApplication(context)
  with AhcWSComponents {

  override lazy val lagomServer: LagomServer = serverFor[JobsScrapperServiceApi](wire[JobsScrapperServiceImpl])

}

class JobsScrapperServiceLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext) = new JobsScrapperApplication(context) {
    override def serviceLocator: ServiceLocator = NoServiceLocator
  }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new JobsScrapperApplication(context) with LagomDevModeComponents

}
