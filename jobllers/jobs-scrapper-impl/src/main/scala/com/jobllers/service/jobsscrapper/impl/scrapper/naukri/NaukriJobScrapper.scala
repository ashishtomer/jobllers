package com.jobllers.service.jobsscrapper.impl.scrapper.naukri

import com.jobllers.service.jobsscrapper.impl.scrapper.JobScrapper

trait NaukriJobScrapper extends JobScrapper with NaukriDotComSeedLinkExtractor {

  /**
    * Here getting the links of the jobs from the 100 seed links only. Also pagination is not considered.
    * Actually, the job scrapper should scrap all the seed links and further pagination links.
    * @return The links of the actual job descriptions.
    */
  def scrapNaukriJobs: List[String] = getJobsSeedLinks().keys.toList.take(100).flatMap(getJobLinks(_).keys.toList)

  private def getJobLinks(pageUri: String) = extractLinksBySelector(pageUri, "div.srp_container.fl div.row a.content")

}
