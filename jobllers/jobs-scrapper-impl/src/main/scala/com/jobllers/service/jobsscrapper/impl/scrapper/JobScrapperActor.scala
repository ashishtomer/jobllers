package com.jobllers.service.jobsscrapper.impl.scrapper

import akka.actor.Actor
import com.jobllers.service.jobsscrapper.impl.scrapper.naukri.NaukriDotComSeedLinkExtractor

sealed trait JobSite
case class Naukri(linkExtractor: NaukriDotComSeedLinkExtractor) extends JobSite

class JobScrapperActor extends Actor {

  override def receive = {
    case jobSite: Naukri ⇒ scrapJobSite(jobSite.linkExtractor)
  }

  private def scrapJobSite(linkExtractor: SeedLinkExtractor): Map[String, String] = linkExtractor.getJobsSeedLinks()

}
