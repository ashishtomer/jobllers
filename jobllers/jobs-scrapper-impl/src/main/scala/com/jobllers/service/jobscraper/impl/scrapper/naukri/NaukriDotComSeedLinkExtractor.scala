package com.jobllers.service.jobscraper.impl.scrapper.naukri

import com.jobllers.service.jobscraper.impl.scrapper.SeedLinkExtractor
import com.jobllers.service.jobscraper.impl.util.RemoteContentPuller
import org.jsoup.Jsoup

//TODO -- Move values stored in NaukriJobSectionsLinks and NaukriSeedLinkSelectors to environment variables

trait NaukriDotComSeedLinkExtractor extends SeedLinkExtractor with RemoteContentPuller {

  import NaukriJobSectionsLinks._
  import NaukriSeedLinkSelectors._
  implicit val headers =  Headers.naukariHeaders

  def getJobsSeedLinks(): List[String] = getJobLinksByLocation(byLocationsLink) ++
    byDesignationLinks.foldRight(List.empty[String])((link, list) ⇒ list ::: getJobLinksByDesignation(link)) ++
    byCompanyLinks.foldRight(List.empty[String])((link, list) ⇒ list ::: getJobLinksByCompany(link)) ++
    getJobLinksByFunctionalAreaCategory(byCategoryLinks) ++ getJobLinksByIndustryCategory(byCategoryLinks) ++
    getTopSkillJobsLinks(byTopSkillsLink)

  def getJobLinksByLocation(uri: String): List[String] = extractLinksBySelector(uri, byLocationSelector)

  def getTopSkillJobsLinks(uri: String): List[String] = extractLinksBySelector(uri, byTopSkillsSelector)

  def getJobLinksByDesignation(uri: String): List[String] = extractLinksBySelector(uri, byDesignationSelector)

  def getJobLinksByCompany(uri: String): List[String] = extractLinksBySelector(uri, byCompanySelector)

  def getJobLinksByFunctionalAreaCategory(uri: String): List[String] = extractLinksBySelector(uri, byFunctionalAreaCategorySelector)

  def getJobLinksByIndustryCategory(uri: String): List[String] = extractLinksBySelector(uri, byIndustryCategorySelector)

}
