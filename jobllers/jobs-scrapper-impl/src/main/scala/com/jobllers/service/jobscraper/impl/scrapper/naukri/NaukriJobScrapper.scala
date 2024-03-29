package com.jobllers.service.jobscraper.impl.scrapper.naukri

import com.jobllers.service.jobscraper.impl.scrapper.JobScrapper
import org.joda.time.DateTime

import scala.collection.mutable

trait NaukriJobScrapper extends JobScrapper with NaukriDotComSeedLinkExtractor {

  /**
    * Here getting the links of the jobs from the 100 seed links only. Also pagination is not considered.
    * Actually, the job scrapper should scrap all the seed links and further pagination links.
    * @return The links of the actual job descriptions.
    */

  var jobLinkCounter = 0
  var jobSiteLinksSet = mutable.LinkedHashSet[String]()

  def scrapNaukriJobs: List[String] = {
    val startTime: Long = DateTime.now().getMillis
    val seedLinks: List[String] = getJobsSeedLinks()
    val endTime: Long = DateTime.now().getMillis
    println(s"\nThe ${seedLinks.size} seed links are extracted in ${endTime - startTime} milliseconds\n")
    seedLinks
    /*val totalJobLinks = seedLinks.flatMap(getJobLinks)
    println(s"\nThe difference of the amount of Links - ${jobLinkCounter - jobSiteLinksSet.size}")
    totalJobLinks.take(100)*/
  }

  private def getJobLinks(pageUri: String) = {
    val jobLinks: List[String] = extractLinksBySelector(pageUri, "div.srp_container.fl div.row a.content")
    jobLinkCounter = jobLinkCounter + jobLinks.size
    jobSiteLinksSet ++= jobLinks
    println(s"\nThe jobLinkCounter - $jobLinkCounter")
    println(s"The unique job Links - ${jobSiteLinksSet.size}")
    jobLinks
  }

}
