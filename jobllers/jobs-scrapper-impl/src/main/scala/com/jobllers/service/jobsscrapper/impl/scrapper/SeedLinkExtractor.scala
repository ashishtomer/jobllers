package com.jobllers.service.jobsscrapper.impl.scrapper

trait SeedLinkExtractor {

  def getJobsSeedLinks(): Map[String, String]

}
